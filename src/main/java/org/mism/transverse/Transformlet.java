package org.mism.transverse;


@FunctionalInterface
public interface Transformlet<In, Out> {
	Out transform(In in, CompositeTransform context);
}