package org.mism.command.templates;

import org.mism.command.CommandTemplate;

public abstract class ModifyCommandTemplate<TargetType, ValueType> implements
		CommandTemplate<TargetType> {

	protected TargetType object;
	protected ValueType oldValue, newValue;

	@Override
	public boolean canUndo() {
		return true;
	}

	public void setNewValue(ValueType newValue) {
		this.newValue = newValue;
	}

	public ValueType getNewValue() {
		return newValue;
	}

	public ValueType getOldValue() {
		return oldValue;
	}

	public TargetType getObject() {
		return object;
	}
}
