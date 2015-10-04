package org.mism.modelgen;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.mism.modelgen.api.Containment;
import org.mism.modelgen.api.Required;

public class Property {
	String name;
	String nameCc;
	boolean required;
	String type;
	String containedType;
	String pkg;
	boolean modelType;
	boolean collection;
	Type parent;
	Type containerType;

	public void init(Type parent, Method m) {
		this.parent = parent;
		name = Type.propertyName(m);
		nameCc = Type.propertyNameCC(m);
		collection = (m.getDeclaredAnnotation(Containment.class) != null) || Collection.class.equals(m.getReturnType());
		if (collection) {
			ParameterizedType retType = (ParameterizedType) m
					.getGenericReturnType();
			Class<?> containedClzz = (Class<?>) (retType
					.getActualTypeArguments()[0]);
			containedType = containedClzz.getSimpleName();

		}
		type = m.getReturnType().getSimpleName();

		pkg = m.getReturnType().getPackage().getName();

		required = (m.getDeclaredAnnotation(Required.class) != null);

	}

	public String getContainedType() {
		return containedType;
	}

	public Type getParent() {
		return parent;
	}

	/**
	 * @return String for import statement.
	 */
	public String getImport() {
		return pkg + "." + type;
	}

	/**
	 * @return fully qualified type descriptor
	 */
	public String getFQN() {
		return pkg + "." + getDeclaration();
	}

	/**
	 * @return type declaration, assuming the type has been imported
	 */
	public String getDeclaration() {
		return type + (collection ? "<" + containedType + ">" : "");
	}

	public String getName() {
		return name;
	}

	public String getNameCc() {
		return nameCc;
	}

	public boolean isRequired() {
		return required;
	}

	public String getType() {
		return type;
	}

	public String getPkg() {
		return pkg;
	}

	public boolean isModelType() {
		return modelType;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setModelType(boolean value) {
		this.modelType = value;
	}

	public void setContainerType(Type type) {
		this.containerType = type;
	}
	
	public Type getContainerType() {
		return containerType;
	}

	public boolean hasContainer()
	{
		return this.containerType != null;
	}
}