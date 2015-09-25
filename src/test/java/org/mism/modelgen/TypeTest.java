package org.mism.modelgen;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mism.modelgen.ifaces.AbstractInterface;
import org.mism.modelgen.ifaces.OtherTestInterface;
import org.mism.modelgen.ifaces.TestInterface;

public class TypeTest {

	@Test
	public void testType() {

		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type type = model.resolve(TestInterface.class);

		assertEquals("TestInterfaceObject", type.getClzzName());
		assertEquals("org.mism.modelgen.ifaces", type.getPackageName());
		assertEquals("[java.math.BigInteger]", type.getImports().toString());
		assertEquals(3, type.getProperties().size());
		assertEquals(1, type.getRequired().length);
		assertSame(type.getRequired(), type.getRequired());
		assertFalse(type.isAbstract());

	}

	@Test
	public void testIsAbstract() {
		Model model = new Model();
		model.init(AbstractInterface.class);
		Type type = new Type();
		type.init(AbstractInterface.class, model);
		assertTrue(type.isAbstract());
	}

}
