package org.mism.modelgen.api;

import java.beans.PropertyChangeSupport;

public interface Mutable {

	PropertyChangeSupport getChangeSupport();

	default void firePropertyChange(String name, Object oldValue,
			Object newValue) {
		getChangeSupport().firePropertyChange(name, oldValue, newValue);
	}

	default void fireIndexedPropertyChange(String name, int index,
			Object oldValue, Object newValue) {
		getChangeSupport().fireIndexedPropertyChange(name, index, oldValue,
				newValue);
	}

}
