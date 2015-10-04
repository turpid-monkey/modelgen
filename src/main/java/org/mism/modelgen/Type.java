package org.mism.modelgen;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mism.modelgen.api.Abstract;
import org.mism.modelgen.api.Contained;
import org.mism.modelgen.api.Containment;

public class Type {

	Class<?> clzz;
 	Model model;
	Collection<String> imports;
	Collection<Property> properties;
	private Property[] required;
	String clzzName;
	String typeName;
	String packageName;
	String simpleName;
	boolean contained;
	Type[] ext3nds;
	boolean abstr4ct;

	public Collection<String> getImports() {
		return imports;
	}

	public Collection<Property> getProperties() {
		return properties;
	}

	public Property[] getRequired() {
		if (required != null)
			return required;
		else
			return required = properties.stream().filter(p -> p.isRequired())
					.toArray(Property[]::new);
	}

	public int getRequiredCount() {
		return getRequired().length;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getClzzName() {
		return clzzName;
	}

	public String getPackageName() {
		return packageName;
	}
	
	public String getJavaFQN()
	{
		return packageName + "." + clzzName;
	}

	public boolean isAbstract() {
		return abstr4ct;
	}

	public boolean isContained() {
		return contained;
	}

	public void init(Class<?> clzz, Model model) {
		assert clzz.isInterface();
		this.clzz = clzz;
		this.model = model;
		contained = Contained.class.isAssignableFrom(clzz);
		properties = initProperties(this, clzz, model);
		imports = initImports(clzz, properties);
		typeName = clzz.getName();
		simpleName = clzz.getSimpleName();
		clzzName = clzz.getSimpleName() + "Object";
		packageName = clzz.getPackage().getName();
		abstr4ct = clzz.getDeclaredAnnotation(Abstract.class) != null;

		ArrayList<Type> ext3nds = new ArrayList<Type>();
		for (Class<?> ifs : clzz.getInterfaces()) {
			if (Contained.class.equals(ifs))
				continue;
			Type parent = model.resolve(ifs);
			if (parent == null)
				throw new IllegalArgumentException("Interface "
						+ clzz.getName() + " extends " + ifs.getName()
						+ ", but this type is not part of the declared model types.");
			properties.addAll(parent.getProperties());
			imports.addAll(parent.getImports());
			ext3nds.add(parent);
		}
		this.ext3nds = ext3nds.toArray(new Type[ext3nds.size()]);
		initContainments(clzz, properties);
	}

	private void initContainments(Class<?> clzz,
			Collection<Property> properties) {
		for (Method m : clzz.getDeclaredMethods()) {
			if (m.getDeclaredAnnotation(Containment.class) != null) {
				Class<?> contained = (Class<?>) ((ParameterizedType) m
						.getGenericReturnType()).getActualTypeArguments()[0];

				List<Property> containers = properties.stream()
						.filter(e -> e.isCollection())
						.collect(Collectors.toList());
				if (containers.size() != 1)
					throw new IllegalArgumentException(
							"Error in @Containment declaration for "
									+ clzz.getName());
				model.addContainment(containers.get(0).getName(), clzz, contained);
				containers.get(0).setContainerType(this);
			}
		}

	}

	static Collection<String> initImports(Class<?> clzz,
			Collection<Property> properties) {
		Set<String> imps = new HashSet<String>();
		for (Property m : properties) {
			if (!"java.lang".equals(m.getPkg())) {
				if (!clzz.getPackage().getName().equals(m.getPkg())) {
					imps.add(m.getImport());
				}
			}
		}
		return imps;
	}

	static Collection<Property> initProperties(Type type, Class<?> clzz,
			Model model) {
		List<Method> list = new ArrayList<Method>();
		for (Method m : clzz.getDeclaredMethods()) {
			if (m.getName().startsWith("get")) {
				list.add(m);
			}
		}
		Collection<Property> props = new ArrayList<Property>();
		for (Method m : list) {
			if (Contained.class.equals(m.getDeclaringClass()))
				continue;
			Property f = new Property();
			f.init(type, m);
			if (model.isModelType(f.getFQN())) {
				f.setModelType(true);
			}
			props.add(f);
		}
		return props;
	}

	static String propertyName(Method m) {
		String name = m.getName().substring(3);
		return name;
	}

	static String propertyNameCC(Method m) {
		String name = propertyName(m);
		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		return name;
	}

	public Type[] getExtended() {
		return ext3nds;
	}

	public String getSimpleName() {
		return simpleName;
	}
	
	public Type getContainer()
	{
		return model.getContainment(this.clzz);
	}

	public Object getContainmentName() {
		return model.getContainmentName(this.clzz);
	}
}
