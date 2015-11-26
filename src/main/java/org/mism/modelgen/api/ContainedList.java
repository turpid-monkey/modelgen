package org.mism.modelgen.api;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ContainedList<P, T extends Contained<P>> implements List<T> {

	private final List<T> delegate;
	private final String name;
	private final P container;

	public ContainedList(String name, P container) {
		this(name, container, new ArrayList<T>());
	}

	public ContainedList(String name, P container, List<T> delegate) {
		this.name = name;
		this.delegate = delegate;
		this.container = container;
	}
	
	public String asJSONString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append('"');
		buf.append(name);
		buf.append("\":[");
		Iterator<T> iter = delegate.iterator();
		while(iter.hasNext())
		{
			buf.append(iter.next().toString());
			if (iter.hasNext()) buf.append(", ");
		}
		buf.append("]");
		return buf.toString();
	}

	public List<T> getContent() {
		return Collections.unmodifiableList(delegate);
	}

	public void add(int index, T element) {
		delegate.add(index, element);
		element.setParent(container);
		fireElementAddedEvent(index, element);
	}

	public boolean add(T o) {
		boolean changed = delegate.add(o);
		if (changed) {
			o.setParent(container);
			fireElementAddedEvent(size() - 1, o);
		}
		return changed;
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean changed = delegate.addAll(c);
		if (changed) {
			c.stream().forEach(elem -> elem.setParent(container));
			fireCumulateChangeEvent();
		}
		return changed;
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		boolean changed = delegate.addAll(c);
		if (changed) {
			c.stream().forEach(elem -> elem.setParent(container));
			fireCumulateChangeEvent();
		}
		return changed;
	}

	public void clear() {
		boolean changed = delegate.size() != 0;
		delegate.clear();
		if (changed)
			fireCumulateChangeEvent();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
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

	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	public T get(int index) {
		return delegate.get(index);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Iterator<T> iterator() {
		return new NotifyingIterator<>(delegate.iterator(), ((Mutable)container).getChangeSupport(), name);
	}

	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	public T remove(int index) {
		T element = delegate.remove(index);
		element.setParent(null);
		fireElementRemovedEvent(index, element);
		return element;
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		int index = delegate.indexOf(o);
		boolean changed = delegate.remove(o);
		if (changed) {
			T t = (T) o;
			t.setParent(null);
			fireElementRemovedEvent(index, t);
		}
		return changed;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean removeAll(Collection<?> c) {
		boolean changed = delegate.removeAll(c);
		if (changed) {
			c.stream().filter(elem -> elem instanceof Contained)
					.forEach(elem -> ((Contained) elem).setParent(container));
			fireCumulateChangeEvent();
		}
		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		boolean changed = delegate.retainAll(c);
		if (changed)
			fireCumulateChangeEvent();
		return changed;
	}

	public T set(int index, T element) {
		T oldValue = delegate.set(index, element);
		element.setParent(container);
		fireElementUpdatedEvent(index, oldValue, element);
		return oldValue;
	}

	public int size() {
		return delegate.size();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <Q> Q[] toArray(Q[] a) {
		return delegate.toArray(a);
	}

	protected void fireIndexedPropertyChangeEvent(int index, T oldValue,
			T newValue) {
		((Mutable)container).fireIndexedPropertyChange(name, index, oldValue, newValue);
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

	@Override
	public ListIterator<T> listIterator() {
		return new NotifyingListIterator<T>(delegate.listIterator(), ((Mutable)container).getChangeSupport(), name);
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return new NotifyingListIterator<T>(delegate.listIterator(index), ((Mutable)container).getChangeSupport(),
				name);
	}

	public PropertyChangeSupport getPropertyChangeSupport() {
		return ((Mutable)container).getChangeSupport();
	}
}
