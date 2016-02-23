package org.mism.modelgen;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CommandLineInterface {
	
	File directory;
	List<Class<?>> clzzes;
	ClassLoader classLoader;

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public List<Class<?>> getClzzes() {
		return clzzes;
	}

	public void setClzzes(List<Class<?>> clzzes) {
		this.clzzes = clzzes;
	}

	static void parseCommandLineArgs(CommandLineInterface cli) {
		String dirStr = System.getProperty("targetDir");
		if (dirStr == null || !(cli.directory = new File(dirStr)).isDirectory()) {
			exit("Missing argument: Use '-DtargetDir=<dir_name>' to define a valid target directory, currently ("
					+ System.getProperty("targetDir")
					+ ").");
		}

		String cpStr = System.getProperty("cp");
		if (cpStr == null || cpStr.trim().isEmpty())
			exit("Missing argument: No classpath defined, use '-Dcp=<class_path>'.");
		String[] cpArray;
		if (cpStr.contains(File.pathSeparator)) {
			cpArray = cpStr.split("" + File.pathSeparatorChar);
		} else {
			cpArray = new String[] { cpStr };
		}
		
		URL[] urls = Stream
				.of(cpArray)
				.map(s -> {
					try {
						return new File(s).toURI().toURL();
					} catch (Exception e) {
						exit(e, "Error in argument: classpath def contains unresolved element '"
								+ s + "'");
						throw new RuntimeException("Aborted code generation.");
					}
				}).toArray(URL[]::new);
		try {
			cli.classLoader = new URLClassLoader(urls, CommandLineInterface.class.getClassLoader());
		} catch (Exception e) {
			exit(e, "Error in argument: Classloader instantiation failed, cp='"
					+ cpArray + "'");
		}

		List<Class<?>> clzzes = new ArrayList<Class<?>>();
		String classArrayStr = System.getProperty("classes");
		if (classArrayStr == null)
			exit("Missing argument: Define '-Dclasses=<class list>', needs to be a comma-separated list of classes.");
		String[] classArray = classArrayStr.split(",");
		for (String clzzName : classArray) {
			try {
				clzzes.add(cli.classLoader.loadClass(clzzName.trim()));
			} catch (Exception e) {
				exit(e, "Error: Cannot instantiate class " + clzzName.trim());
			}
		}
		cli.clzzes = clzzes;
	}

	public void generateCode() throws Exception {
		ModelGenerator generator = new ModelGenerator();
		FileResourceSet resourceSet = new FileResourceSet(directory);
		generator.generate(resourceSet,
				clzzes.toArray(new Class[clzzes.size()]));
	}

	public static void main(String[] argv) {
		CommandLineInterface cli = new CommandLineInterface();
		parseCommandLineArgs(cli);

		try {

			msg("Generating classes " + cli.clzzes + " to directory "
					+ cli.directory.getAbsolutePath());
			cli.generateCode();
		} catch (Exception e) {
			exit(e, "Error during code generation: " + e.getMessage());
		}

		exit("Done.");
	}

	static void exit(String msg) {
		msg(msg);
		System.exit(0);
	}
	
	static void exit(Exception e, String msg)
	{
		e.printStackTrace();
		exit(msg);
	}

	static void msg(String msg) {
		System.out.println(msg);
	}

}
