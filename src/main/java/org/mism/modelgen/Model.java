package org.mism.modelgen;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Model {

	String packageName;
	Map<String, Type> types = new HashMap<String, Type>();
	Class<?>[] interfaces;

	public void init(Class<?>... interfaces) {
		this.interfaces = interfaces;
		for (Class<?> clzz : interfaces) {
			if (!clzz.isInterface())
				throw new IllegalArgumentException(
						"Model may only contain interfaces.");
			initType(clzz);

		}
		packageName = interfaces[0].getPackage().getName();
		// resolve
	}

	public String getPackage() {
		return packageName;
	}

	public Type initType(Class<?> clzz) {
		Type t = new Type();
		types.put(clzz.getName(), t);
		t.init(clzz, this);
		return t;
	}

	public Collection<Type> getTypes() {
		return types.values();
	}

	public Type resolve(Class<?> clzz) {
		if (types.containsKey(clzz.getName()))
			return types.get(clzz.getName());
		else {
			return initType(clzz);
		}
	}

	public boolean isModelType(String type) {
		for (Class<?> clzz : interfaces) {
			if (clzz.getName().equals(type))
				return true;
			for (Class<?> ifc : clzz.getInterfaces()) {
				if (ifc.getName().equals(type))
					return true;
			}
		}
		return false;
	}

}
