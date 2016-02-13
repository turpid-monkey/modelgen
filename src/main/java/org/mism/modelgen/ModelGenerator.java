package org.mism.modelgen;

import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class ModelGenerator {

    private static final String TEMPLATE_PATH = "org/mism/modelgen/vmtemplates/";
    private VelocityEngine engine;
   
    public ModelGenerator(VelocityEngine engine)
    {
    	this.engine = engine;
    }
    
    public ModelGenerator()
    {
    	try {
			this.engine = createDefaultEngine();
		} catch (Exception e) {
			throw new RuntimeException("Could not initiate default velocity engine.", e);
		}
    }
    
    protected void mergeTemplate(String name, VelocityContext ctx, Writer out) throws Exception
    {
    	String path = resolveTemplatePath(name);
    	engine.mergeTemplate(path, "UTF-8", ctx, out);
    }
    
    public static String resolveTemplatePath(String templateName)
    {
    	return TEMPLATE_PATH + templateName + ".vm";
    }
    
    public static VelocityEngine createDefaultEngine() throws Exception
    {
    	Properties props = new Properties();
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        props.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        return new VelocityEngine(props);
    }

    public void generate(ResourceSet set, Class<?>... classes) throws Exception {
        Model model = new Model();
        model.init(classes);
        for (Type t : model.getTypes()) {
            Resource res = set.open(t.getPackageName() + "." + t.getClzzName());
            Writer out = res.open();
            generateType(out, t);
            res.close();
        }
        Resource factoryRes = set.open(model.getPackage() + ".ModelFactory");
        Writer out = factoryRes.open();
        generateFactory(out, model);
        factoryRes.close();
        
        Resource visitorRes = set.open(model.getPackage() + ".ModelVisitor");
        out = visitorRes.open();
        generateVisitor(out, model);
        visitorRes.close();
        
        Resource commandRes = set.open(model.getPackage() + ".ModelCommandFactory");
        out = commandRes.open();
        generateCommands(out, model);
        commandRes.close();
    }

    public void generateType(Writer out, Type t) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("type", t);
        mergeTemplate("class", context, out);
    }

    public void generateFactory(Writer out, Model model) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("model", model);
        mergeTemplate("factory", context, out);
    }

    public void generateVisitor(Writer out, Model model) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("model", model);
        mergeTemplate("visitor", context, out);
    }

    public void generateCommands(Writer out, Model model) throws Exception {
        VelocityContext context = new VelocityContext();
        context.put("model", model);
        mergeTemplate("commands", context, out);
    }

}
