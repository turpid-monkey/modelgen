package org.mism.modelgen.api;

public interface Cloneable<T> {

	T shallowClone();

	T deepClone();

}
