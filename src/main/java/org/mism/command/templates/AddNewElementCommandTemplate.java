package org.mism.command.templates;

import java.util.Collection;

import org.mism.command.CommandTemplate;

public abstract class AddNewElementCommandTemplate<TargetType, ValueType>
		implements CommandTemplate<TargetType> {

	protected ValueType newElement;
	protected TargetType object;
	protected Collection<ValueType> collection;

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void execute() {
		collection.add(newElement);
	}

	@Override
	public void rollback() {
		collection.remove(newElement);
	}
}
