package org.mism.modelgen;

import java.io.PrintWriter;

public interface Resource {
	
	PrintWriter open();
	void close();

}
