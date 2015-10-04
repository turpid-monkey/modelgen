package org.mism.modelgen;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mism.modelgen.ifaces.ChildInterface;
import org.mism.modelgen.ifaces.ParentInterface;
import org.mism.modelgen.ifaces.TestInterface;

public class PropertyTest {

	@Test
	public void testPropertyAll() throws Exception {
		Property p = new Property();
		p.init(null, TestInterface.class.getMethod("getName"));

		assertEquals("Name", p.getName());
		assertEquals("name", p.getNameCc());
		assertEquals("java.lang", p.getPkg());
		assertEquals("String", p.getType());
		assertTrue(p.isRequired());
		assertFalse(p.isCollection());
	}

	@Test
	public void testPropertyIsRequired() throws Exception {
		Property p = new Property();
		p.init(null, TestInterface.class.getMethod("getID"));

		assertEquals("ID", p.getName());
		assertEquals("iD", p.getNameCc());
		assertEquals("java.math", p.getPkg());
		assertEquals("BigInteger", p.getType());
		assertFalse(p.isRequired());
	}

	@Test
	public void testContainment() throws Exception {
		Property p = new Property();
		p.init(null, ParentInterface.class.getMethod("getChildren"));

		assertTrue(p.isCollection());
		assertEquals("ChildInterface", p.containedType);
		assertEquals("java.util.Collection<ChildInterface>", p.getFQN());
	}

	@Test
	public void testContainmentHasContainer() throws Exception {
		Property p = new Property();
		p.init(null, ParentInterface.class.getMethod("getChildren"));

		assertTrue(p.isCollection());
	}

}
