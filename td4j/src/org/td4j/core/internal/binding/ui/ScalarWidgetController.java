/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008 Michael Rauch

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

package org.td4j.core.internal.binding.ui;

import org.td4j.core.binding.model.ICaption;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeEventFilter;
import org.td4j.core.model.IObserver;
import org.td4j.core.reflect.ReflectionTK;



public abstract class ScalarWidgetController<W> implements IObserver {

	private final ScalarDataProxy dataProxy;

	private ICaption caption;

	private boolean modelUpdateInProgress;
	private boolean viewUpdateInProgress;

	protected ScalarWidgetController(ScalarDataProxy proxy) {
		dataProxy = proxy;
		dataProxy.addObserver(this, new ChangeEventFilter(dataProxy, ChangeEvent.Type.StateChange));
	}

	public ScalarDataProxy getDataProxy() {
		return dataProxy;
	}

	public void observableChanged(ChangeEvent event) {
		updateView();
	}

	// PEND: würde gebraucht, wenn VERBOSE PropertyChangeEvent empfangen würde ...
	// ist aber glaub ich nicht der fall
	// public void propertyChanged(PropertyChangeEvent evt) {
	// if (evt.getType() == PropertyChangeEvent.Type.SHORT) {
	// updateView();
	// } else {
	// updateView(evt.getNewValue());
	// }
	// }

	protected void updateCaption() {
		final String name = ReflectionTK.humanize(dataProxy.getName());
		if (caption != null) caption.setText(name);
	}

	protected void updateView() {
		if (modelUpdateInProgress) return;
		if (getWidget() == null) return; // during construction phase

		setAccess();
		updateView(dataProxy.readValue());
	}

	protected void updateView(Object newValue) {
		if (modelUpdateInProgress) return;

		viewUpdateInProgress = true;
		try {
			updateView0(newValue);
		} finally {
			viewUpdateInProgress = false;
		}
	}

	protected void updateModel() {
		if (viewUpdateInProgress) return;
		if ( ! canWrite()) return;

		modelUpdateInProgress = true;
		try {
			dataProxy.writeValue(updateModel0());
		} finally {
			modelUpdateInProgress = false;
		}

		// refetch the display value from the model
		updateView();
	}

	protected boolean isViewUpdateInProgress() {
		return viewUpdateInProgress;
	}

	protected boolean isModelUpdateInProgress() {
		return modelUpdateInProgress;
	}

	public abstract W getWidget();

	public ICaption getCaption() {
		return caption;
	}

	public void setCaption(ICaption caption) {
		this.caption = caption;
		updateCaption();
	}

	// PEND: better naming for those methods
	protected abstract void updateView0(Object newValue);

	protected abstract Object updateModel0();

	protected abstract void setAccess();

	protected boolean canWrite() {
		return getDataProxy() != null && getDataProxy().canWrite();
	}

	protected boolean canRead() {
		return getDataProxy() != null && getDataProxy().canRead();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + dataProxy.getName();
	}

}
