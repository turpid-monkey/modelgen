package org.mism.modelgen.ifaces;

import org.mism.modelgen.api.Contained;

public interface Branch extends Contained<TreeSegment> {
	
	int getLength();

}
