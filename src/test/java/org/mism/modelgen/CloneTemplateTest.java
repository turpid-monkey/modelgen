package org.mism.modelgen;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.mism.modelgen.ifaces.OtherTestInterface;
import org.mism.modelgen.ifaces.TestInterface;

public class CloneTemplateTest {

	@Test
	public void testCloneCode() throws Exception {
		ModelGenerator generator = new ModelGenerator();

		StringWriter out = new StringWriter();
		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type t = model.resolve(TestInterface.class);

		VelocityContext context = new VelocityContext();
		context.put("type", t);
		generator.mergeTemplate("class_clone", context, out);

		assertEquals("    // start Clonable implementation        public TestInterface shallowClone() {"
				+ "        TestInterfaceObject cl = new TestInterfaceObject();"
				+ "        cl.name = this.name;" + "        cl.iD = this.iD;"
				+ "        cl.other = this.other;" + "        return cl;"
				+ "    }" + "    " + "    public TestInterface deepClone() {"
				+ "        TestInterfaceObject cl = new TestInterfaceObject();"
				+ "        cl.name = this.name;" + "        cl.iD = this.iD;"
				+ "        cl.other = (OtherTestInterface) ((Clonable)this.other).deepClone();"
				+ "        return cl;" + "    }        // end Clonable implementation",
				out.toString().replace("\n", ""));
	}

}
