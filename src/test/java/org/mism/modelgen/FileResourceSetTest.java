package org.mism.modelgen;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.file.Files;

import org.junit.Test;

public class FileResourceSetTest {

	@Test
	public void computeNameTest() throws Exception {
		File tempDir = Files.createTempDirectory("testFileResource").toFile();
		tempDir.deleteOnExit();

		FileResourceSet set = new FileResourceSet(tempDir);
		File file = set.computeFileNameFromJavaFQN("some.package.Test");
		assertEquals("Test.java", file.getName());
		assertTrue(file.getAbsolutePath().startsWith(tempDir.getAbsolutePath()));
		assertEquals("/some/package/Test.java", file.getAbsolutePath()
				.substring(tempDir.getAbsolutePath().length()));

		// clear up temp folders
		File pkg1Folder = file.getParentFile();
		File pkg2Folder = pkg1Folder.getParentFile();

		Files.delete(pkg1Folder.toPath());
		Files.delete(pkg2Folder.toPath());
		Files.delete(tempDir.toPath());
	}
	
	@Test
	public void openResourceTest() throws Exception
	{
		File tempDir = Files.createTempDirectory("testFileResource").toFile();
		tempDir.deleteOnExit();

		FileResourceSet set = new FileResourceSet(tempDir);

		Resource res = set.open("some.package.Test");
		String clazzSource = "public class Test { public void test(){ System.out.println(\"test\");} }";
		PrintWriter out = res.open();
		out.println(clazzSource);
		res.close();
		
		File file = ((FileResource)res).getFile();
		FileReader in = new FileReader(file);		
		CharBuffer buf = CharBuffer.allocate(clazzSource.length()+16);
		in.read(buf);
		in.close();
		buf.rewind();
		assertEquals(clazzSource, buf.toString().trim());
		
		// clear up temp folders
		File pkg1Folder = file.getParentFile();
		File pkg2Folder = pkg1Folder.getParentFile();

		Files.delete(file.toPath());
		Files.delete(pkg1Folder.toPath());
		Files.delete(pkg2Folder.toPath());
		Files.delete(tempDir.toPath());
	}
}
