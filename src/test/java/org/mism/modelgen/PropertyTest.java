package org.mism.modelgen;

import static org.junit.Assert.*;

import org.junit.Test;

public class PropertyTest {

	@Test
	public void testPropertyAll() throws Exception {
		Property p = new Property();
		p.init(TestInterface.class.getMethod("getName"));

		assertEquals("Name", p.getName());
		assertEquals("name", p.getNameCc());
		assertEquals("java.lang", p.getPkg());
		assertEquals("String", p.getType());
		assertTrue(p.isRequired());
	}
	

	@Test
	public void testPropertyIsRequired() throws Exception {
		Property p = new Property();
		p.init(TestInterface.class.getMethod("getID"));

		assertEquals("ID", p.getName());
		assertEquals("iD", p.getNameCc());
		assertEquals("java.math", p.getPkg());
		assertEquals("BigInteger", p.getType());
		assertFalse(p.isRequired());
	}

}
