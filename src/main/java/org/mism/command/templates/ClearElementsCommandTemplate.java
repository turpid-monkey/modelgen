package org.mism.command.templates;

import java.util.Collection;

import org.mism.command.CommandTemplate;

public abstract class ClearElementsCommandTemplate<TargetType, ValueType> implements
		CommandTemplate<TargetType> {

	protected TargetType object;
	protected Collection<ValueType> collectionRef;
	protected Collection<ValueType> collectionClone;

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void execute() {
		collectionRef.clear();
	}

	@Override
	public void rollback() {
		collectionRef.addAll(collectionClone);
	}
}
