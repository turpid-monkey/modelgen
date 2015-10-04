package org.mism.modelgen.api;

import static org.junit.Assert.*;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NotifyingIteratorTest {

	Iterator<String> langIter;
	List<String> backEnd;
	NotifyingIterator<String> iter;
	PropertyChangeSupport pcs;

	@Before
	public void setUp() {
		backEnd = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
		langIter = backEnd.iterator();
		pcs = new PropertyChangeSupport(backEnd);
		iter = new NotifyingIterator<>(langIter, pcs, "test");
	}

	@Test
	public void testIteratorUsage() throws Exception {
		int i = 0;
		for (String e : iter) {
			assertEquals(backEnd.get(i++), e);
		}
		assertEquals(4, i);
	}

	@Test
	public void testRemove() {
		pcs.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				assertTrue(evt instanceof IndexedPropertyChangeEvent);
				IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
				assertEquals(1, iEvt.getIndex());
				assertNull(iEvt.getNewValue());
				assertEquals("b", iEvt.getOldValue());
				throw new RuntimeException("OK");
			}
		});
		assertEquals("a", iter.next());
		assertEquals("b", iter.next());
		try {
			iter.remove();
			fail("Property change event not triggered");
		} catch (Exception e) {
			assertEquals("OK", e.getMessage());
			assertEquals(3, backEnd.size());
			assertEquals("c", backEnd.get(1));
		}
		assertEquals("c", iter.next());
		assertEquals("d", iter.next());
		assertFalse(iter.hasNext());
	}
}
