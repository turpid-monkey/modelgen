package org.mism.modelgen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mism.modelgen.api.Abstract;

public class Type {

	Model model;
	Collection<String> imports;
	Collection<Property> properties;
	private Property[] required;
	String clzzName;
	String typeName;
	String packageName;
	String simpleName;
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

	public boolean isAbstract() {
		return abstr4ct;
	}

	public void init(Class<?> clzz, Model model) {
		assert clzz.isInterface();
		this.model = model;
		properties = initProperties(clzz, model);
		imports = initImports(clzz, properties);
		typeName = clzz.getName();
		simpleName = clzz.getSimpleName();
		clzzName = clzz.getSimpleName() + "Object";
		packageName = clzz.getPackage().getName();
		abstr4ct = clzz.getDeclaredAnnotation(Abstract.class) != null;

		ArrayList<Type> ext3nds = new ArrayList<Type>();
		for (Class<?> ifs : clzz.getInterfaces()) {
			Type parent = model.resolve(ifs);
			properties.addAll(parent.getProperties());
			imports.addAll(parent.getImports());
			ext3nds.add(parent);
		}
		this.ext3nds = ext3nds.toArray(new Type[ext3nds.size()]);
		
	}

	static Collection<String> initImports(Class<?> clzz,
			Collection<Property> properties) {
		Collection<String> imps = new ArrayList<String>();
		for (Property m : properties) {
			if (!"java.lang".equals(m.getPkg())) {
				if (!clzz.getPackage().getName().equals(m.getPkg()))
					imps.add(m.getFQN());
			}
		}
		return imps;
	}

	static Collection<Property> initProperties(Class<?> clzz, Model model) {
		List<Method> list = new ArrayList<Method>();
		for (Method m : clzz.getDeclaredMethods()) {
			if (m.getName().startsWith("get")) {
				list.add(m);
			}
		}
		Collection<Property> props = new ArrayList<Property>();
		for (Method m : list) {
			Property f = new Property();
			f.init(m);
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
}
