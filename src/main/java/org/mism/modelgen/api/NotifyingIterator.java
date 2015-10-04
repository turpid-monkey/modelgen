package org.mism.modelgen.api;

import java.beans.PropertyChangeSupport;
import java.util.Iterator;

class NotifyingIterator<T> implements Iterator<T>, Iterable<T> {
	final Iterator<T> delegate;
	final PropertyChangeSupport pcs;
	final String name;
	T last;
	int cursor = -1;

	public NotifyingIterator(Iterator<T> delegate,
			PropertyChangeSupport pcs, String name) {
		this.delegate = delegate;
		this.pcs = pcs;
		this.name = name;
	}

	public boolean hasNext() {
		return delegate.hasNext();
	}

	public T next() {
		cursor++;
		return last = delegate.next();
	}

	public void remove() {
		delegate.remove();
		pcs.fireIndexedPropertyChange(name, cursor--, last, null);
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}
}