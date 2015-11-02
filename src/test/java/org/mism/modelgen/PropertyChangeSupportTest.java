package org.mism.modelgen;

import static org.junit.Assert.*;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mism.modelgen.api.ContainedList;
import org.mism.modelgen.api.Mutable;
import org.mism.modelgen.ifaces.ChildInterface;
import org.mism.modelgen.ifaces.ParentInterface;
import org.mism.modelgen.ifaces.TestInterface2;

public class PropertyChangeSupportTest {

	@Test
	public void testBeanPropertyChangeEvent() throws Exception {
		TestInterface2 testy = ModelGeneratorTest
				.classify(TestInterface2.class);
		List<PropertyChangeEvent> changeEvents = new ArrayList<PropertyChangeEvent>();
		((Mutable) testy).getChangeSupport().addPropertyChangeListener(
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						changeEvents.add(evt);
					}

				});
		testy.getClass().getMethod("setValue1", String.class)
				.invoke(testy, "New");
		assertEquals(1, changeEvents.size());
		assertEquals("Value1", changeEvents.get(0).getPropertyName());
		assertEquals(null, changeEvents.get(0).getOldValue());
		assertEquals("New", changeEvents.get(0).getNewValue());

	}

	@Test
	public void testIndexedPropertyChangeEvent() throws Exception {
		ParentInterface testy = ModelGeneratorTest.classify(
				ParentInterface.class, ChildInterface.class);
		List<PropertyChangeEvent> changeEvents = new ArrayList<PropertyChangeEvent>();
		((Mutable) testy).getChangeSupport().addPropertyChangeListener(
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						changeEvents.add(evt);
					}

				});
		ContainedList<ParentInterface, ChildInterface> clist = (ContainedList) testy
				.getClass().getMethod("getChildrenRef").invoke(testy);
		ChildInterface testChild = new ChildInterface() {

			ParentInterface p;

			@Override
			public ParentInterface getParent() {
				return p;
			}

			@Override
			public void setParent(ParentInterface parent) {
				p = parent;

			}

			@Override
			public String getName() {
				return "Testy";
			}
		};
		clist.add(testChild);
		assertEquals(1, changeEvents.size());
		assertTrue(changeEvents.get(0) instanceof IndexedPropertyChangeEvent);
		IndexedPropertyChangeEvent evt = (IndexedPropertyChangeEvent) changeEvents
				.get(0);
		assertEquals("Children", evt.getPropertyName());
		assertEquals(null, evt.getOldValue());
		assertSame(testChild, evt.getNewValue());
		assertEquals(0, evt.getIndex());
		assertSame(testChild.getParent(), testy);
	}

}
