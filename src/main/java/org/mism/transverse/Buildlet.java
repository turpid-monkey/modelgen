package org.mism.transverse;



@FunctionalInterface
public interface Buildlet<In, Out> {
	Out build(In in);
}