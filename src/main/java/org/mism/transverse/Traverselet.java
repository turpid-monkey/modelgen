package org.mism.transverse;


@FunctionalInterface
public interface Traverselet<In> {
	void traverse(In o, Traverse ctx);
}