package org.mism.modelgen;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.Test;
import org.mism.modelgen.ifaces.OtherTestInterface;
import org.mism.modelgen.ifaces.TestInterface;

public class CloneTemplateTest {

	@Test
	public void testCloneCode() throws Exception {
		Properties props = new Properties();
		props.setProperty("file.resource.loader.path", ModelGenerator.class
				.getResource(".").getFile());
		Velocity.init(props);
		VelocityContext context = new VelocityContext();

		StringWriter out = new StringWriter();
		Model model = new Model();
		model.init(TestInterface.class, OtherTestInterface.class);
		Type t = model.resolve(TestInterface.class);

		context.put("type", t);
		Velocity.mergeTemplate("class_clone.vm", "UTF-8", context, out);

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
