package org.mism.command;

public interface CommandTemplate<T> extends Command<T> {
	
	public void prepare(T object);

}
