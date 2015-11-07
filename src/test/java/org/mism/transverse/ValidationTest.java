package org.mism.transverse;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValidationTest {

	@Test
	public void testValidation() {
		Traverselet<String> t1 = (in, ctx) -> {
			for (int i = 0; i < in.length(); i++)
				ctx.traverse(in.charAt(i));
		};
		Validatelet<Character> v1 = (in, ctx) -> {
			if (in == 'a')
				ctx.fail("No 'a' allowed");
		};
		ValidationHook hook = new ValidationHook(v1);
		Traverse traverse = new SimpleCompositeTraverse(hook, t1);
		traverse.traverse("test");
		assertEquals(0, hook.getMessages().size());
		traverse.traverse("taste");
		assertEquals(1, hook.getMessages().size());
		hook.reset();
		traverse.traverse("a tasty test");
		assertEquals(2, hook.getMessages().size());
		hook.reset();
		traverse.traverse('a');
		assertEquals(1, hook.getMessages().size());
	}

}
