package org.mism.modelgen.ifaces;

import org.mism.modelgen.api.Containment;

public interface ClassNode {
	
	@Containment MethodNode getMethodNode();

}
