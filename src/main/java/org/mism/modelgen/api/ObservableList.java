package org.mism.modelgen.api;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObservableList<T> implements List<T> {

	final List<T> delegate;
	final String propertyName;
	final PropertyChangeSupport pcs;

	public ObservableList(String propertyName, PropertyChangeSupport pcs,
			List<T> delegate) {
		this.delegate = delegate;
		this.pcs = pcs;
		this.propertyName = propertyName;
	}

	public ObservableList(String propertyName, PropertyChangeSupport pcs) {
		this(propertyName, pcs, new ArrayList<T>());
	}

	@Override
	public Iterator<T> iterator() {
		return new NotifyingIterator<>(delegate.iterator(), pcs, propertyName);
	}

	@Override
	public Object[] toArray() {
		return delegate.toArray();
	}

	@Override
	public <Ox1337> Ox1337[] toArray(Ox1337[] a) {
		return delegate.toArray(a);
	}

	@Override
	public boolean add(T e) {
		boolean changed = delegate.add(e);
		fireElementAddedEvent(size() - 1, e);
		return changed;
	}
	
	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	@Override
	public boolean remove(Object o) {
		T t = (T) o;
		int index = delegate.indexOf(t);
		boolean changed = delegate.remove(t);
		if (changed) {
			
			fireElementRemovedEvent(index, t);
		}
		return changed;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean changed = delegate.addAll(c);
		if (changed) {

			fireCumulateChangeEvent();
		}
		return changed;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		boolean changed = delegate.addAll(index, c);
		if (changed) {

			fireCumulateChangeEvent();
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = delegate.removeAll(c);
		if (changed) {
			
			fireCumulateChangeEvent();
		}
		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		boolean changed = delegate.retainAll(c);
		if (changed)
			fireCumulateChangeEvent();
		return delegate.retainAll(c);
	}

	@Override
	public void clear() {
		boolean changed = delegate.size() != 0;
		delegate.clear();
		if (changed)
			fireCumulateChangeEvent();
	}

	@Override
	public T get(int index) {
		return delegate.get(index);
	}

	@Override
	public T set(int index, T element) {
		T oldValue = delegate.set(index, element);
		fireElementUpdatedEvent(index, oldValue, element);
		return oldValue;
	}

	@Override
	public void add(int index, T element) {
		delegate.add(index, element);
		fireElementAddedEvent(index, element);
	}

	@Override
	public T remove(int index) {
		T element = delegate.remove(index);
		fireElementRemovedEvent(index, element);
		return element;
	}

	@Override
	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	@Override
	public ListIterator<T> listIterator() {
		return new NotifyingListIterator<T>(delegate.listIterator(), pcs,
				propertyName);
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return new NotifyingListIterator<T>(delegate.listIterator(index), pcs,
				propertyName);
	}

	PropertyChangeSupport getChangeSupport() {
		return pcs;
	}

	public String asJSONString() {
		StringBuffer buf = new StringBuffer();
		buf.append('"');
		buf.append(propertyName);
		buf.append("\":[");
		Iterator<T> iter = delegate.iterator();
		while (iter.hasNext()) {
			buf.append(iter.next().toString());
			if (iter.hasNext())
				buf.append(", ");
		}
		buf.append("]");
		return buf.toString();
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public List<T> getContent() {
		return Collections.unmodifiableList(delegate);
	}

	protected void fireIndexedPropertyChangeEvent(int index, T oldValue,
			T newValue) {
		pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	private void fireCumulateChangeEvent() {
		fireIndexedPropertyChangeEvent(-1, null, null);
	}

	private void fireElementUpdatedEvent(int index, T oldValue, T element) {
		fireIndexedPropertyChangeEvent(index, oldValue, element);
	}

	private void fireElementRemovedEvent(int index, T element) {
		fireIndexedPropertyChangeEvent(index, element, null);
	}

	private void fireElementAddedEvent(int index, T element) {
		fireIndexedPropertyChangeEvent(index, null, element);
	}
	
	public boolean replaceContent(Collection<? extends T> c) {
		if (!delegate.equals(c)) {
			delegate.clear();
			delegate.addAll((Collection<? extends T>) c);

			fireCumulateChangeEvent();
			return true;
		}
		return false;
	}
}
