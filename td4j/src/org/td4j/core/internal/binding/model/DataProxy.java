/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

  td4j is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  td4j is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with td4j.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/

package org.td4j.core.internal.binding.model;

import org.td4j.core.binding.IModelSocket;
import org.td4j.core.internal.model.ChangeEventImpl;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.Observable;
import org.td4j.core.model.ObservableTK;
import org.td4j.core.tk.ObjectTK;


public abstract class DataProxy extends Observable implements IModelSocket, IObserver {

	private final String name;
	private Object model;

	/**
	 * @param name
	 *          may be null
	 */
	protected DataProxy(String name) {
		this.name = name;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		modelChanged(getModel(), model);
	}

	private void modelChanged(Object prevModel, Object model) {
		final Class<?> modelType = getModelType();

		if (model != null && ! modelType.isAssignableFrom(model.getClass())) {
			throw new IllegalArgumentException("type mismatch: " + model.getClass().getName() + " != " + modelType.getName());
		}

		if ( ! ObjectTK.equal(prevModel, model)) {
			if (prevModel != null) {
				ObservableTK.detachObserverFromModel(prevModel, this);
			}

			this.model = model;

			if (model != null) {
				ObservableTK.attachObserverToModel(model, this);
			}

			changeSupport.fireStateChange();
		}
	}

	public void refreshFromModel() {
		changeSupport.fireStateChange();
	}

	protected void valueModified() {
		final DataProxyChangeEvent event = new DataProxyChangeEvent(this);
		event.setCustomPayload(DataProxyChangeEvent.CustomEvent.ValueModified);
		changeSupport.fire(event);
	}

	public void observableChanged(ChangeEvent event) {
		if (event.getSource() != model) return;

		switch (event.getType()) {
		case StateChange:
			changeSupport.fireStateChange();
			break;
		case LazyPropertyChange: // fallthrough
		case PropertyChange:
			if (event.contains(name)) changeSupport.fireStateChange();
			break;
		}
	}

	public String getName() {
		return name;
	}


	public static class DataProxyChangeEvent extends ChangeEventImpl {
		public static enum CustomEvent {
			ValueModified
		}

		private DataProxyChangeEvent(DataProxy source) {
			super(source, ChangeEvent.Type.Custom);
		}
	}

}
