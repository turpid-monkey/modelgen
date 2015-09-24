package org.mism.modelgen;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.Test;

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

		assertEquals("    public Object clone() {"
				+ "        TestInterfaceObject cl = new TestInterfaceObject();"
				+ "        cl.name = this.name;" + "        cl.iD = this.iD;"
				+ "        cl.other = this.other;" + "        return cl;"
				+ "    }" + "    " + "    public Object deepClone() {"
				+ "        TestInterfaceObject cl = new TestInterfaceObject();"
				+ "        cl.name = this.name;" + "        cl.iD = this.iD;"
				+ "        cl.other = this.other.deepClone();"
				+ "        return cl;" + "    }",
				out.toString().replace("\n", ""));
	}

}
