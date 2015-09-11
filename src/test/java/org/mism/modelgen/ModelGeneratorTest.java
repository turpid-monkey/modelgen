package org.mism.modelgen;

import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

public class ModelGeneratorTest extends ModelGenerator {

	@Test
	public void testGenerateClass() throws Exception {

		ModelGenerator generator = new ModelGenerator();
		StringWriter out = new StringWriter();
		generator.generate(out, TestInterface.class);

		assertEquals("", out.toString());
	}

}
