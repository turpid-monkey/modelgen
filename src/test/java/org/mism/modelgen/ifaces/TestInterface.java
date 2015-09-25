package org.mism.modelgen.ifaces;

import java.math.BigInteger;

import org.mism.modelgen.api.Required;

public interface TestInterface {

	@Required
	String getName();

	BigInteger getID();

	OtherTestInterface getOther();

}
