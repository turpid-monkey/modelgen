package org.mism.modelgen.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mism.modelgen.ifaces.ChildInterface;
import org.mism.modelgen.ifaces.ParentInterface;

public class ContainedListTest {

	class Parent implements ParentInterface, Mutable {
		ContainedList<ParentInterface, ChildInterface> children = new ContainedList<ParentInterface, ChildInterface>(
				"children", this);

		PropertyChangeSupport pcs = new PropertyChangeSupport(this);
		
		@Override
		public Collection<ChildInterface> getChildren() {
			return children;
		}

		protected ContainedList<ParentInterface, ChildInterface> children() {
			return children;
		}

		@Override
		public PropertyChangeSupport getChangeSupport() {
			return pcs;
		}


	}

	class Child implements ChildInterface {

		ParentInterface parent;

		@Override
		public ParentInterface getParent() {
			return parent;
		}

		@Override
		public void setParent(ParentInterface parent) {
			this.parent = parent;
		}

		@Override
		public String getName() {
			return "test";
		}
		
		public String toString()
		{
			return getName();
		}

	}

	@Test
	public void testAdd() throws Exception {

		Parent parent = new Parent();
		Child child = new Child();
		parent.children().getPropertyChangeSupport()
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						assertTrue(evt instanceof IndexedPropertyChangeEvent);
						IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
						assertEquals(0, iEvt.getIndex());
						assertNull(iEvt.getOldValue());
						assertSame(child, iEvt.getNewValue());
						assertSame(parent, child.getParent());
						throw new RuntimeException("All is well!");
					}
				});
		try {
			parent.children().add(child);
			fail("Property change listener not notified");
		} catch (RuntimeException e) {
			assertEquals("All is well!", e.getMessage());
		}
	}

	@Test
	public void testRemove() throws Exception {
		Parent parent = new Parent();
		Child child = new Child();
		parent.children().add(child);
		parent.children().getPropertyChangeSupport()
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						assertTrue(evt instanceof IndexedPropertyChangeEvent);
						IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
						assertEquals(0, iEvt.getIndex());
						assertNull(iEvt.getNewValue());
						assertSame(child, iEvt.getOldValue());
						assertSame(null, child.getParent());
						throw new RuntimeException("All is well!");
					}
				});
		try {
			parent.children().remove(child);
			fail("Property change listener not notified");
		} catch (RuntimeException e) {
			assertEquals("All is well!", e.getMessage());
			assertEquals(0, parent.children().size());
		}
	}

	@Test
	public void testRemove0() throws Exception {
		Parent parent = new Parent();
		Child child = new Child();
		parent.children().add(child);
		parent.children().getPropertyChangeSupport()
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						assertTrue(evt instanceof IndexedPropertyChangeEvent);
						IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
						assertEquals(0, iEvt.getIndex());
						assertNull(iEvt.getNewValue());
						assertSame(child, iEvt.getOldValue());
						assertSame(null, child.getParent());
						throw new RuntimeException("All is well!");
					}
				});
		try {
			parent.children().remove(0);
			fail("Property change listener not notified");
		} catch (RuntimeException e) {
			assertEquals("All is well!", e.getMessage());
			assertEquals(0, parent.children().size());
		}
	}

	@Test
	public void testClear() {
		Parent parent = new Parent();
		Child child = new Child();
		parent.children().add(child);
		parent.children().getPropertyChangeSupport()
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						assertTrue(evt instanceof IndexedPropertyChangeEvent);
						IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
						assertEquals(-1, iEvt.getIndex());
						assertNull(iEvt.getNewValue());
						assertNull(iEvt.getOldValue());
						throw new RuntimeException("All is well!");
					}
				});
		try {
			parent.children().clear();
			fail("Property change listener not notified");
		} catch (RuntimeException e) {
			assertEquals("All is well!", e.getMessage());
			assertEquals(0, parent.children().size());
		}
	}

	@Test
	public void testRemoveAll() {
		Parent parent = new Parent();
		Child child = new Child();
		parent.children().add(child);

		List<Object> removes = new ArrayList<Object>();
		removes.add(child);
		removes.add("decoy");

		parent.children().getPropertyChangeSupport()
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						assertTrue(evt instanceof IndexedPropertyChangeEvent);
						IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
						assertEquals(-1, iEvt.getIndex());
						assertNull(iEvt.getNewValue());
						assertNull(iEvt.getOldValue());
						throw new RuntimeException("All is well!");
					}
				});
		try {
			parent.children().removeAll(removes);
			fail("Property change listener not notified");
		} catch (RuntimeException e) {
			assertEquals("All is well!", e.getMessage());
			assertEquals(0, parent.children().size());
		}
	}

	@Test
	public void testRetainAll() {
		Parent parent = new Parent();
		Child child = new Child();
		parent.children().add(child);

		List<Object> retainables = new ArrayList<Object>();
		retainables.add(child);
		retainables.add("decoy");

		parent.children().getPropertyChangeSupport()
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						assertTrue(evt instanceof IndexedPropertyChangeEvent);
						IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
						assertEquals(-1, iEvt.getIndex());
						assertNull(iEvt.getNewValue());
						assertNull(iEvt.getOldValue());
						throw new RuntimeException("All is well!");
					}
				});
		try {
			boolean changed = parent.children().retainAll(retainables); // nop
			assertFalse(changed);
			retainables.remove(child);
			parent.children().retainAll(retainables); // child is removed =
														// event!
			fail("Property change listener not notified");
		} catch (RuntimeException e) {
			assertEquals("All is well!", e.getMessage());
			assertEquals(0, parent.children().size());
		}
	}

	@Test
	public void testReplaceContent() {
		Parent parent = new Parent();
		Child child1 = new Child(), child2 = new Child(), child3 = new Child();
		parent.children().add(child1);
		parent.children().add(child2);

		assertFalse(parent.children().replaceContent(
				Arrays.asList(child1, child2)));
		assertTrue(parent.children().replaceContent(
				Arrays.asList(child2, child3)));
		assertTrue(parent.children().equals(Arrays.asList(child2, child3)));
	}
	
	@Test
	public void testAsJSON()
	{
		Parent parent = new Parent();
		Child child1 = new Child(), child2 = new Child();
		parent.children().add(child1);
		parent.children().add(child2);
		assertEquals("\"children\":[test, test]", parent.children().asJSONString());
	}

	@Test
	public void testSet() {
		Parent parent = new Parent();
		Child child1 = new Child(), child2 = new Child(), child3 = new Child();
		parent.children().add(child1);
		parent.children().add(child2);

		parent.children().getPropertyChangeSupport()
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						assertTrue(evt instanceof IndexedPropertyChangeEvent);
						IndexedPropertyChangeEvent iEvt = (IndexedPropertyChangeEvent) evt;
						assertEquals(1, iEvt.getIndex());
						assertEquals(child3, iEvt.getNewValue());
						assertEquals(child2, iEvt.getOldValue());
						throw new RuntimeException("All is well!");
					}
				});
		try {
			parent.children().set(1, child3);
			fail("Property change listener not notified");
		} catch (RuntimeException e) {
			assertEquals("All is well!", e.getMessage());
			assertEquals(2, parent.children().size());
			assertEquals(child1, parent.children().get(0));
			assertEquals(child3, parent.children().get(1));
			assertSame(parent, child3.getParent());
		}
	}
}
