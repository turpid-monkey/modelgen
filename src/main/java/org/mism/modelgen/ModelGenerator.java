package org.mism.modelgen;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.mism.modelgen.api.Required;

public class ModelGenerator {

	public void generate(ResourceSet set, Class... classes) throws Exception {
		Model model = new Model();
		model.init(classes);
		for (Type t : model.getTypes()) {

			Resource res = set.open(t.getPackageName() + "." + t.getClzzName());
			Writer out = res.open();
			generateType(out, t);
			res.close();
		}
	}

	public void generateType(Writer out, Type t) throws Exception {
		Properties props = new Properties();
		props.setProperty("file.resource.loader.path", ModelGenerator.class
				.getResource(".").getFile());

		Velocity.init(props);

		VelocityContext context = new VelocityContext();

		context.put("type", t);

		Velocity.mergeTemplate("class.vm", "UTF-8", context, out);

	}
	
	public void generateFactory(Writer out, Model model ) throws Exception {
		Properties props = new Properties();
		props.setProperty("file.resource.loader.path", ModelGenerator.class
				.getResource(".").getFile());

		Velocity.init(props);

		VelocityContext context = new VelocityContext();

		context.put("model", model);

		Velocity.mergeTemplate("builder.vm", "UTF-8", context, out);
	}

}
