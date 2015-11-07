package org.mism.command;

public interface Command<T> {
	
	boolean canUndo();
	void execute();
	void rollback();

}
