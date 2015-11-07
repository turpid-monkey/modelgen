package org.mism.transverse;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CompositeTraverseTransformBuildTest {

	static Transformlet<Integer, String> integerToString() {
		return (in, ctx) -> {
			return in.toString();
		};
	}

	static Transformlet<List<Integer>, List<String>> listToList() {
		return (in, ctx) -> {
			List<String> r = new ArrayList<String>();
			for (Integer i : in) {
				r.add(ctx.transform(i, Integer.class, String.class));
			}
			return r;
		};
	}

	@Test
	public void testTransformation() {
		CompositeTransform tf = new CompositeTransform(integerToString(),
				listToList());
		String s = tf
				.transform(Integer.valueOf(5), Integer.class, String.class);
		assertEquals("5", s);

		List<String> ls = tf.transform(Arrays.asList(1, 2, 3), List.class,
				List.class);
		assertEquals(3, ls.size());
		assertEquals(String.class, ls.get(0).getClass());
		assertEquals("[1, 2, 3]", ls.toString());
	}

	@Test
	public void testTraverse() {
		StringBuffer buf = new StringBuffer();
		Traverselet<String> t1 = (in, ctx) -> {
			buf.append(in);
			ctx.traverse(in.length());
		};
		Traverselet<Integer> t2 = (in, ctx) -> {
			buf.append(in);
		};
		Traverse tr = new SimpleCompositeTraverse(t1, t2);
		tr.traverse("test");
		assertEquals("test4", buf.toString());
	}

	@Test
	public void testTraverseAndBuild() {
		Traverselet<String> t1 = (in, ctx) -> {
			ctx.traverse(in.length());
		};
		Buildlet<String, List> b1 = (in) -> {
			List<String> res = new ArrayList();
			res.add(in);
			return res;
		};
		Buildlet<Integer, List> b2 = (in) -> {
			List<Integer> res = new ArrayList();
			res.add(in);
			return res;
		};
		BuilderHook hook = new BuilderHook(b1, b2);
		Traverse ct = new SimpleCompositeTraverse(hook, t1);
		ct.traverse("Hello");
		List res = hook.getResult("Hello", List.class);
		assertEquals("Hello", res.get(0));
		res = hook.getResult(5, List.class);
		assertEquals(5, res.get(0));

	}
}
