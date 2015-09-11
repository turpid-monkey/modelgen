package org.mism.modelgen;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class ModelGenerator {

	public static class Util {

		Class clzz;

		public Util(Class clzz) {
			this.clzz = clzz;
		}

		public Collection<String> getImports() {
			Collection<String> imps = new ArrayList<String>();
			for (Method m : getFields()) {
				if (m.getName().startsWith("get")
						&& !m.getReturnType().getName().startsWith("java.lang")) {
					imps.add(m.getReturnType().getName());
				}
			}
			return imps;
		}

		public Collection<Method> getRequiredFields() {
			List<Method> list = new ArrayList<Method>();
			for (Method m : clzz.getDeclaredMethods()) {
				if (m.getName().startsWith("get")
						&& m.getDeclaredAnnotation(Required.class) != null) {
					list.add(m);
				}
			}
			return list;
		}

		public Collection<Method> getFields() {
			List<Method> list = new ArrayList<Method>();
			for (Method m : clzz.getDeclaredMethods()) {
				if (m.getName().startsWith("get")) {
					list.add(m);
				}
			}
			return list;
		}

		public String fieldName(Method m) {
			String name = m.getName().substring(3);

			return name;
		}

		public String fieldNameCC(Method m) {
			String name = fieldName(m);
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			return name;
		}
	}

	public void generate(ResourceSet set, Class... classes) throws Exception {
		for (Class clzz : classes) {
			if (!clzz.isInterface())
				throw new IllegalArgumentException(
						"Only interfaces can be used");
			Resource res = set.open(clzz.getName());
			Writer out = res.open();
			generate(out, clzz);
			res.close();
		}
	}

	public void generate(Writer out, Class clzz) throws Exception {
		Properties props = new Properties();
		props.setProperty("file.resource.loader.path", ModelGenerator.class
				.getResource(".").getFile());

		Velocity.init(props);

		VelocityContext context = new VelocityContext();
		context.put("class", clzz);
		context.put("util", new Util(clzz));

		clzz.getMethods()[0].getAnnotation(Required.class);

		Velocity.mergeTemplate("class.vm", "UTF-8", context, out);

	}

}
