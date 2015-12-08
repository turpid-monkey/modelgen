package org.mism.modelgen.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContainedList<P, T extends Contained<P>> extends ObservableList<T> {

	private final P container;

	public ContainedList(String name, P container) {
		this(name, container, new ArrayList<T>());
	}

	public ContainedList(String name, P container, List<T> delegate) {
		super(name, ((Mutable)container).getChangeSupport(), delegate);
		this.container = container;
	}

	public void add(int index, T element) {
		element.setParent(container);
		super.add(index, element);
	}

	public boolean add(T o) {
		o.setParent(container);
		return super.add(o);
	}

	public boolean addAll(Collection<? extends T> c) {
		c.stream().forEach(elem -> elem.setParent(container));
		return super.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		c.stream().forEach(elem -> elem.setParent(container));
		return super.addAll(index, c);
	}

	public void clear() {
		super.clear();
	}

	public T remove(int index) {
		T element = super.get(index);
		element.setParent(null);
		return super.remove(index);
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		T t = (T) o;
		t.setParent(null);
		return super.remove(t);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean removeAll(Collection<?> c) {
		c.stream().filter(elem -> elem instanceof Contained)
		.forEach(elem -> ((Contained) elem).setParent(container));
		return super.removeAll(c);
	}

	public T set(int index, T element) {
		element.setParent(container);
		return super.set(index, element);
	}
}
