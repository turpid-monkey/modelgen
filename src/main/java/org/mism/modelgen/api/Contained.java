package org.mism.modelgen.api;

public interface Contained<T> {

	T getParent();

	void setParent(T parent);

}
