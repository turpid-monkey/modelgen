package org.mism.modelgen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandLineInterface {

	static void exit(String msg) {
		msg(msg);
		System.exit(0);
	}

	static void msg(String msg) {
		System.out.println(msg);
	}

	public static void main(String[] argv) {
		if (argv.length < 2)
			exit("Usage: <target-dir> [<class>]+");
		File dir = new File(argv[0]);
		if (!dir.isDirectory()) {
			exit("Error: <target-dir>=" + argv[0] + " needs to be a directory");
		}
		List<Class<?>> clzzes = new ArrayList<Class<?>>();
		for (int i = 1; i < argv.length; i++) {
			try {
				clzzes.add(Class.forName(argv[i]));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				exit("Error: Cannot instantiate class " + argv[i] );
			}
		}
		msg("Generating classes " + clzzes + " to directory "
				+ dir.getAbsolutePath());
		ModelGenerator generator = new ModelGenerator();
		FileResourceSet resourceSet = new FileResourceSet(dir);
		try {
			generator.generate(resourceSet,
					clzzes.toArray(new Class[clzzes.size()]));
		} catch (Exception e) {
			e.printStackTrace();
			exit("Failure: Generator failed with message " + e.getMessage());
		}
		exit("Done.");
	}

}
