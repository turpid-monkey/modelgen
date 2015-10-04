package org.mism.modelgen.api;

public interface Clonable<T> {

	T shallowClone();

	T deepClone();

}
