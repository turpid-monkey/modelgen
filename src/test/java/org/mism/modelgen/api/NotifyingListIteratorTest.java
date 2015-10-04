package org.mism.modelgen.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

public class NotifyingListIteratorTest {
	ListIterator<String> langIter;
	List<String> backEnd;
	NotifyingListIterator<String> iter;
	PropertyChangeSupport pcs;

	@Before
	public void setUp() {
		backEnd = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
		langIter = backEnd.listIterator();
		pcs = new PropertyChangeSupport(backEnd);
		iter = new NotifyingListIterator<String>(langIter, pcs, "test");
	}

	@Test
	public void testIteratorUsage() throws Exception {
		int i = 0;
		for (String e : iter) {
			assertEquals(backEnd.get(i++), e);
		}
		assertEquals(4, i);
		while (iter.hasPrevious()) {
			assertEquals(--i, iter.previousIndex());
			assertEquals(backEnd.get(i), iter.previous());
		}
	}

	@Test
	public void testSet() {
		pcs.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				assertTrue(evt instanceof IndexedPropertyChangeEvent);
				IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
				assertEquals(1, iEvt.getIndex());
				assertEquals("b", iEvt.getOldValue());
				assertEquals("B", iEvt.getNewValue());
				throw new RuntimeException("OK");
			}
		});
		assertEquals("a", iter.next());
		assertEquals("b", iter.next());
		try {
			iter.set("B");
			fail("Property change event not triggered");
		} catch (Exception e) {
			assertEquals("OK", e.getMessage());
			assertEquals(4, backEnd.size());
			assertEquals("B", backEnd.get(1));
		}
		assertEquals("c", iter.next());
		assertEquals("d", iter.next());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testAdd() {
		pcs.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				assertTrue(evt instanceof IndexedPropertyChangeEvent);
				IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
				assertEquals(2, iEvt.getIndex());
				assertNull(iEvt.getOldValue());
				assertEquals("b+0.5", iEvt.getNewValue());
				throw new RuntimeException("OK");
			}
		});
		assertEquals("a", iter.next());
		assertEquals("b", iter.next());
		try {
			iter.add("b+0.5");
			fail("Property change event not triggered");
		} catch (Exception e) {
			assertEquals("OK", e.getMessage());
			assertEquals(5, backEnd.size());
			assertEquals("b+0.5", backEnd.get(2));
		}
		assertEquals("c", iter.next());
		assertEquals("d", iter.next());
		assertFalse(iter.hasNext());
	}
}
