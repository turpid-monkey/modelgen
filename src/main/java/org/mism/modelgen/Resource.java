package org.mism.modelgen;

import java.io.IOException;
import java.io.PrintWriter;

public interface Resource {

	PrintWriter open() throws IOException;

	void close() throws IOException;

}
