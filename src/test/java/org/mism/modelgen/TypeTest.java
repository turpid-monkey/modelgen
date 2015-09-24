package org.mism.modelgen;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeTest {

	@Test
	public void testType() {

		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type type = model.resolve(TestInterface.class);

		assertEquals("TestInterfaceObject", type.getClzzName());
		assertEquals("org.mism.modelgen", type.getPackageName());
		assertEquals("[java.math]", type.getImports().toString());
		assertEquals(3, type.getProperties().size());
		assertEquals(1, type.getRequired().length);
		assertSame(type.getRequired(), type.getRequired());
		assertFalse(type.isAbstract());

	}

	@Test
	public void testIsAbstract() {
		Type type = new Type();
		type.init(AbstractInterface.class, new Model());
		assertTrue(type.isAbstract());
	}

}
