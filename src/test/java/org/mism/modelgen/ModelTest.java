package org.mism.modelgen;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModelTest {

	@Test
	public void testModelAll() {
		Model model = new Model();
		model.init(AbstractInterface.class, TestInterface.class);

		assertEquals(2, model.getTypes().size());

		Type t = model.resolve(AbstractInterface.class);
		assertEquals(AbstractInterface.class.getName(), t.getTypeName());

	}

	@Test
	public void testModelWithExtendingInterface() {
		Model model = new Model();

		model.init(ExtendingInterface.class, AbstractInterface.class,
				TestInterface.class);
		
		Type t = model.resolve(ExtendingInterface.class);
		assertEquals(2, t.getExtended().length);
	}

}
