package org.mism.modelgen.ifaces;

import java.util.Collection;

import org.mism.modelgen.api.Contained;
import org.mism.modelgen.api.Containment;

public interface DoubleContainmentPerson extends Contained<DoubleContainmentPerson> {
	@Containment
	DoubleContainmentPerson getMother();

	@Containment
	DoubleContainmentPerson getFather();

	@Containment
	Collection<DoubleContainmentPerson> getKids();
	
	@Containment
	Collection<DoubleContainmentPerson> getSlaves();
}