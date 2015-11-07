package org.mism.command;

import java.util.ArrayList;
import java.util.List;

public interface CommandFactory {

	List<Class<? extends CommandTemplate<?>>> getCommandTemplates(
			Class<?> modelClass);

	default <T> List<Command<T>> prepareCommands(Class<T> modelClass, T obj) {
		List<Command<T>> commands = new ArrayList<>();
		List<Class<? extends CommandTemplate<?>>> commandTemplates = getCommandTemplates(modelClass);
		for (Class<? extends CommandTemplate<?>> clz : commandTemplates) {
			try {
				CommandTemplate<T> t;
				t = (CommandTemplate<T>) clz.newInstance();
				t.prepare(obj);
				commands.add(t);
			} catch (Exception e) {
				// Sorry I can't do that Dave.
				// This conversation can serve no purpose anymore.
			}
		}
		return commands;
	}

}
