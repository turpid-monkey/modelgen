package org.mism.modelgen;

import java.lang.reflect.Method;

import org.mism.modelgen.api.Required;

public class Property {
	String name;
	String nameCc;
	boolean required;
	String type;
	String pkg;
	boolean modelType;

	public void init(Method m) {
		name = Type.propertyName(m);
		nameCc = Type.propertyNameCC(m);
		type = m.getReturnType().getSimpleName();
		pkg = m.getReturnType().getPackage().getName();
		required = (m.getDeclaredAnnotation(Required.class) != null);
	}

	public String getFQN() {
		return pkg + "." + type;
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

	public void setModelType(boolean b) {
		this.modelType = b;
	}

	public boolean getModelType() {
		return modelType;
	}

}