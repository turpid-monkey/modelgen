package org.mism.modelgen.api;

@FunctionalInterface
public interface Condition<E> {
	
	boolean test(E object);

}
