package org.mism.modelgen;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mism.modelgen.ifaces.AbstractInterface;
import org.mism.modelgen.ifaces.ChildInterface;
import org.mism.modelgen.ifaces.ClassNode;
import org.mism.modelgen.ifaces.ExtendingInterface;
import org.mism.modelgen.ifaces.MethodNode;
import org.mism.modelgen.ifaces.ParentInterface;
import org.mism.modelgen.ifaces.TestInterface;

public class ModelTest {
	
	@Test
	public void testSimpleContainment() throws Exception
	{
		Model model = new Model();
        model.init(MethodNode.class, ClassNode.class);
        Type methodNode = model.resolve(MethodNode.class);
        assertTrue(methodNode.isContained());
        Type classNode = model.resolve(ClassNode.class);
        assertTrue(classNode.getProperties().iterator().next().hasContainer());
	}


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

	@Test
	public void testModelResolutionOfContainedTypes() {
		Model model = new Model();

		model.init(ParentInterface.class, ChildInterface.class);

		Type t = model.resolve(ParentInterface.class);
		assertEquals(1, t.getProperties().size());
		assertEquals("ChildInterfaceObject", t.getProperties().iterator()
				.next().getContainedTypeDescr().getClzzName());
	}

}
