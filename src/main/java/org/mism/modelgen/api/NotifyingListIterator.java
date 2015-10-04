package org.mism.modelgen.api;

import java.beans.PropertyChangeSupport;
import java.util.ListIterator;

class NotifyingListIterator<T> extends NotifyingIterator<T> implements
		ListIterator<T> {
	ListIterator<T> delegate;

	public NotifyingListIterator(ListIterator<T> delegate,
			PropertyChangeSupport pcs, String name) {
		super(delegate, pcs, name);
		this.delegate = delegate;
	}

	@Override
	public boolean hasPrevious() {
		return delegate.hasPrevious();
	}

	@Override
	public T previous() {
		cursor--;
		return last = delegate.previous();
	}

	@Override
	public int nextIndex() {
		return delegate.nextIndex();
	}

	@Override
	public int previousIndex() {
		return delegate.previousIndex();
	}

	@Override
	public void set(T e) {
		delegate.set(e);
		pcs.fireIndexedPropertyChange(name, cursor, last, e);
	}

	@Override
	public void add(T e) {
		delegate.add(e);
		pcs.fireIndexedPropertyChange(name, cursor + 1, null, e);
	}

}