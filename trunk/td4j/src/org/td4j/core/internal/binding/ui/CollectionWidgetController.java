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

package org.td4j.core.internal.binding.ui;

import java.util.Collection;

import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.binding.model.ICaption;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeEventFilter;
import org.td4j.core.model.IObserver;
import org.td4j.core.reflect.ReflectionTK;



// PEND: code reading, make as immutable as possible
// PEND: parts are common with scalar
public abstract class CollectionWidgetController<W> implements IObserver {

	private final ListDataProxy dataProxy;

	private ICaption caption;

	private boolean viewUpdateInProgress;

	protected CollectionWidgetController(ListDataProxy proxy) {
		this.dataProxy = proxy;
		proxy.addObserver(this, new ChangeEventFilter(dataProxy, ChangeEvent.Type.StateChange));
	}

	public ListDataProxy getDataProxy() {
		return dataProxy;
	}

	// PEND: remove ?!
	// public void setValueProxy(ICollectionValue proxy) {
	// if (valueProxy != null) valueProxy.removeStateChangeHandler(this);
	// valueProxy = proxy;
	// if (valueProxy != null) valueProxy.addStateChangeHandler(this);
	//		
	// setAccess();
	// updateView();
	// updateCaption();
	// }

	public void observableChanged(ChangeEvent event) {
		updateView();
	}

	protected void updateCaption() {
		if (caption != null) {
			final String name = ReflectionTK.humanize(dataProxy.getName());
			caption.setText(name);
		}
	}

	protected void updateView() {
		if (getWidget() == null) return; // during construction phase

		setAccess();
		updateView(dataProxy.readValue());
	}

	protected void updateView(Collection<?> newValue) {
		viewUpdateInProgress = true;
		try {
			updateView0(newValue);
		} finally {
			viewUpdateInProgress = false;
		}
	}

	protected boolean isViewUpdateInProgress() {
		return viewUpdateInProgress;
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
	protected abstract void updateView0(Collection<?> newValue);

	protected abstract void setAccess();

	protected boolean canRead() {
		return getDataProxy() != null && getDataProxy().canRead();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + dataProxy.getName();
	}

}