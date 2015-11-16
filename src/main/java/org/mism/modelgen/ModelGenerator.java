package org.mism.modelgen;

import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class ModelGenerator {

    public static final String TEMPLATE_PATH = "org/mism/modelgen/vmtemplates/";

    public void generate(ResourceSet set, Class<?>... classes) throws Exception {
        Model model = new Model();
        model.init(classes);
        for (Type t : model.getTypes()) {

            Resource res = set.open(t.getPackageName() + "." + t.getClzzName());
            Writer out = res.open();
            generateType(out, t);
            res.close();
        }
    }

    private String getPath() {
        return ModelGenerator.class.getResource("templates").getFile();
    }

    public void generateType(Writer out, Type t) throws Exception {
        Properties props = new Properties();
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        props.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());

        Velocity.init(props);

        VelocityContext context = new VelocityContext();

        context.put("type", t);

        Velocity.mergeTemplate(TEMPLATE_PATH + "class.vm", "UTF-8", context, out);

    }

    public void generateFactory(Writer out, Model model) throws Exception {
        Properties props = new Properties();
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        props.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());

        Velocity.init(props);

        VelocityContext context = new VelocityContext();

        context.put("model", model);

        Velocity.mergeTemplate(TEMPLATE_PATH + "factory.vm", "UTF-8", context, out);
    }

    public void generateVisitor(Writer out, Model model) throws Exception {
        Properties props = new Properties();
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        props.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());

        Velocity.init(props);

        VelocityContext context = new VelocityContext();

        context.put("model", model);

        Velocity.mergeTemplate(TEMPLATE_PATH + "visitor.vm", "UTF-8", context, out);
    }

    public void generateCommands(Writer out, Model model) throws Exception {
        Properties props = new Properties();
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        props.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        
        Velocity.init(props);

        VelocityContext context = new VelocityContext();

        context.put("model", model);

        Velocity.mergeTemplate(TEMPLATE_PATH + "commands.vm", "UTF-8", context, out);

    }

}
