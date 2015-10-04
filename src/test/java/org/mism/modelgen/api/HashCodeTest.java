package org.mism.modelgen.api;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class HashCodeTest {
	
	@Test public void testHashCode() throws Exception
	{
		String[] hashMe = new String[]{"hash", "it", "plox"};
		List<String> hashMeList = Arrays.asList(hashMe);
		
		assertEquals(82761722, ObjectUtil.newHash().hash(hashMe).value());
		assertEquals(82761722, ObjectUtil.newHash().hash(hashMeList).value());
		assertEquals(82761722, ObjectUtil.newHash().hash((Object)hashMe).value());
	}

}
