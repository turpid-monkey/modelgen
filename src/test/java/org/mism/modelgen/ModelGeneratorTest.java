package org.mism.modelgen;

import static org.junit.Assert.*;
import groovy.lang.GroovyClassLoader;

import java.io.StringWriter;
import java.math.BigInteger;

import org.junit.Test;
import org.mdkt.compiler.InMemoryJavaCompiler;
import org.mism.modelgen.api.Cloneable;

public class ModelGeneratorTest extends ModelGenerator {

	@Test
	public void testGenerateClass() throws Exception {

		ModelGenerator generator = new ModelGenerator();
		StringWriter out = new StringWriter();
		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type t = model.resolve(TestInterface.class);
		generator.generateType(out, t);

		System.out.println(out);

		Class clazz = InMemoryJavaCompiler.compile(
				"org.mism.modelgen.TestInterfaceObject", out.toString());
		TestInterface test = (TestInterface) clazz.newInstance();

		assertNotNull(test);

	}

	@Test
	public void testShallowCloning() throws Exception {
		ModelGenerator generator = new ModelGenerator();
		StringWriter out = new StringWriter();
		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type t = model.resolve(TestInterface.class);
		generator.generateType(out, t);

		Class clazz = InMemoryJavaCompiler.compile(
				"org.mism.modelgen.TestInterfaceObject", out.toString());
		TestInterface test = (TestInterface) clazz.newInstance();

		out = new StringWriter();
		generator.generateType(out, model.resolve(OtherTestInterface.class));
		System.out.println(out);
		Class otherClazz = InMemoryJavaCompiler.compile(
				"org.mism.modelgen.OtherTestInterfaceObject", out.toString());

		OtherTestInterface other = (OtherTestInterface) otherClazz
				.newInstance();

		test.getClass().getMethod("setName", String.class).invoke(test, "test");
		test.getClass().getMethod("setID", BigInteger.class)
				.invoke(test, BigInteger.valueOf(37));
		test.getClass().getMethod("setOther", OtherTestInterface.class)
				.invoke(test, other);

		TestInterface cloned = (TestInterface) ((Cloneable) test)
				.shallowClone();
		assertEquals(cloned.getID(), test.getID());
		assertEquals(cloned.getName(), test.getName());
		assertNotSame(cloned, test);
		assertSame(cloned.getOther(), other);
	}

	@Test
	public void testDeepCloning() throws Exception {
		ModelGenerator generator = new ModelGenerator();
		StringWriter out = new StringWriter();
		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type t = model.resolve(TestInterface.class);
		generator.generateType(out, t);

		Class clazz = InMemoryJavaCompiler.compile(
				"org.mism.modelgen.TestInterfaceObject", out.toString());
		TestInterface test = (TestInterface) clazz.newInstance();

		out = new StringWriter();
		generator.generateType(out, model.resolve(OtherTestInterface.class));
		System.out.println(out);
		Class otherClazz = InMemoryJavaCompiler.compile(
				"org.mism.modelgen.OtherTestInterfaceObject", out.toString());

		OtherTestInterface other = (OtherTestInterface) otherClazz
				.newInstance();

		test.getClass().getMethod("setName", String.class).invoke(test, "test");
		test.getClass().getMethod("setID", BigInteger.class)
				.invoke(test, BigInteger.valueOf(37));
		test.getClass().getMethod("setOther", OtherTestInterface.class)
				.invoke(test, other);

		TestInterface cloned = (TestInterface) ((Cloneable) test).deepClone();
		assertEquals(cloned.getID(), test.getID());
		assertEquals(cloned.getName(), test.getName());
		assertNotSame(cloned, test);
		assertNotSame(cloned.getOther(), other);
	}

	@Test
	public void testEquals() throws Exception {
		ModelGenerator generator = new ModelGenerator();
		StringWriter out = new StringWriter();
		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type t = model.resolve(TestInterface.class);
		generator.generateType(out, t);

		System.out.println(out);

		Class clazz = InMemoryJavaCompiler.compile(
				"org.mism.modelgen.TestInterfaceObject", out.toString());
		TestInterface test1 = (TestInterface) clazz.newInstance();
		TestInterface test2 = (TestInterface) clazz.newInstance();
		test1.getClass().getMethod("setName", String.class)
				.invoke(test1, "test");
		test1.getClass().getMethod("setID", BigInteger.class)
				.invoke(test1, BigInteger.valueOf(37));

		test1.getClass().getMethod("setName", String.class)
				.invoke(test2, "test");
		test1.getClass().getMethod("setID", BigInteger.class)
				.invoke(test2, BigInteger.valueOf(37));

		assertEquals(test1, test2);
		assertEquals(test2, test1);

		test1.getClass().getMethod("setID", BigInteger.class)
				.invoke(test2, BigInteger.valueOf(39));

		assertNotEquals(test1, test2);
	}

	@Test
	public void testHashCode() throws Exception {
		ModelGenerator generator = new ModelGenerator();
		StringWriter out = new StringWriter();
		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type t = model.resolve(TestInterface.class);
		generator.generateType(out, t);

		System.out.println(out);

		Class clazz = InMemoryJavaCompiler.compile(
				"org.mism.modelgen.TestInterfaceObject", out.toString());
		TestInterface test1 = (TestInterface) clazz.newInstance();
		test1.getClass().getMethod("setName", String.class)
				.invoke(test1, "test");
		test1.getClass().getMethod("setID", BigInteger.class)
				.invoke(test1, BigInteger.valueOf(37));

		assertNotEquals(test1.hashCode(), System.identityHashCode(test1));
		TestInterface clone = (TestInterface) ((Cloneable) test1)
				.shallowClone();
		assertEquals(test1.hashCode(), clone.hashCode());

		test1.getClass().getMethod("setID", BigInteger.class)
				.invoke(test1, BigInteger.valueOf(39));

		assertNotEquals(test1, clone);
	}
}
