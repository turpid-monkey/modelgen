package org.mism.modelgen.ifaces;

import java.util.Collection;

import org.mism.modelgen.api.Containment;

public interface ParentInterface {
	
	@Containment Collection<ChildInterface> getChildren();

}
