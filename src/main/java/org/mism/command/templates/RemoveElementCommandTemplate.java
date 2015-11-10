package org.mism.command.templates;

import java.util.Collection;
import java.util.Collections;

import org.mism.command.CommandTemplate;

public abstract class RemoveElementCommandTemplate<TargetType, ValueType>
		implements CommandTemplate<TargetType> {

	protected TargetType object;
	protected ValueType element;
	protected Collection<ValueType> collectionRef;

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void execute() {
		collectionRef.remove(element);
	}

	@Override
	public void rollback() {
		collectionRef.add(element);
	}

	public void setElement(ValueType element) {
		if (collectionRef.contains(element)) {
			this.element = element;
		} else {
			throw new IllegalArgumentException(
					"Cannot remove this element (anymore?) from the underlying collection.");
		}
	}

	public ValueType getElement() {
		return element;
	}

	public Collection<ValueType> getCollection() {
		return Collections.unmodifiableCollection(collectionRef);
	}
}
