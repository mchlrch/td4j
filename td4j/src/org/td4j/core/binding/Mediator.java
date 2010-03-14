/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

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

package org.td4j.core.binding;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.internal.binding.model.DataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeEventFilter;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.Observable;
import org.td4j.core.tk.ObjectTK;



public class Mediator<T> extends Observable implements IModelSocket {

	private final List<IModelSocket> sockets = new ArrayList<IModelSocket>();
	private final Class<T> modelType;
	private T model;

	private final LoopbackObserver loopbackObserver = new LoopbackObserver(this);

	public Mediator(Class<T> modelType) {
		this.modelType = ObjectTK.enforceNotNull(modelType, "modelType");
	}

	public Class<?> getModelType() {
		return modelType;
	}

	public T getModel() {
		return model;
	}

	public void setModel(Object model) {
		final ChangeEvent changeEvent = changeSupport.preparePropertyChange("model", this.model, model);
		if (changeEvent == null) return;
		
		if (model != null && ! modelType.isAssignableFrom(model.getClass())) {
			throw new IllegalArgumentException("type mismatch: " + model.getClass().getName() + " != " + modelType.getName());
		}

		this.model = (T) model;
		
		for (IModelSocket delegate : sockets) {
			delegate.setModel(model);
		}
		
		changeSupport.fire(changeEvent);
	}

	public void refreshFromModel() {
		for (IModelSocket delegate : sockets) {
			delegate.refreshFromModel();
		}
		
		changeSupport.fireStateChange();
	}

	// interface to controller delegates
	// PEND: ev. mit callback methode auf delegate prüfen, ob delegate nicht schon
	// an anderem mediator angehängt ist (ScalarWidgetController.bindPlug())
	public void addModelSocket(IModelSocket delegate) {
		if ( ! sockets.contains(delegate)) {
			addLoopbackObserver(delegate);
			sockets.add(delegate);
			delegate.setModel(model);
		}
	}

	public void removeModelSocket(IModelSocket delegate) {
		sockets.remove(delegate);
		removeLoopbackObserver(delegate);
		delegate.setModel(null);
	}

	private void addLoopbackObserver(IModelSocket delegate) {
		if (delegate instanceof DataProxy) {
			final DataProxy proxy = (DataProxy) delegate;
			proxy.addObserver(loopbackObserver, new ChangeEventFilter(proxy, ChangeEvent.Type.Custom));
		}
	}

	private void removeLoopbackObserver(IModelSocket delegate) {
		if (delegate instanceof DataProxy) {
			final DataProxy proxy = (DataProxy) delegate;
			proxy.removeObserver(loopbackObserver);
		}
	}


	private static class LoopbackObserver implements IObserver {
		private final Mediator<?> mediator;

		private LoopbackObserver(Mediator<?> mediator) {
			this.mediator = ObjectTK.enforceNotNull(mediator, "mediator");
		}

		public void observableChanged(ChangeEvent event) {
			if (DataProxy.DataProxyChangeEvent.CustomEvent.ValueModified == event.getCustomPayload() && mediator.sockets.contains(event.getSource())) {
				mediator.refreshFromModel();
			}
		}
	}

}
