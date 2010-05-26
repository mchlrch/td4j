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

package org.td4j.core.internal.binding.ui;

import org.td4j.core.binding.model.Caption;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeEventFilter;
import org.td4j.core.model.IObserver;
import org.td4j.core.reflect.ReflectionTK;



public abstract class IndividualWidgetController<W> implements IObserver {

	private final IndividualDataProxy dataProxy;

	private Caption caption;

	private boolean modelUpdateInProgress;
	private boolean viewUpdateInProgress;

	protected IndividualWidgetController(IndividualDataProxy proxy) {
		dataProxy = proxy;
		dataProxy.addObserver(this, new ChangeEventFilter(dataProxy, ChangeEvent.Type.StateChange));
	}

	public IndividualDataProxy getDataProxy() {
		return dataProxy;
	}

	public void observableChanged(ChangeEvent event) {
		updateView();
	}

	protected void updateCaption() {
		final String name = ReflectionTK.humanize(dataProxy.getName());
		if (caption != null) caption.setText(name);
	}

	protected void updateView() {
		if (modelUpdateInProgress) return;
		if (getWidget() == null) return; // during construction phase

		setAccess();
		
		final Object value = dataProxy.canRead() ? dataProxy.readValue() : null;
		updateView(value);
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

	public Caption getCaption() {
		return caption;
	}

	public void setCaption(Caption caption) {
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
