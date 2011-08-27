/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2010 Michael Rauch

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
import org.td4j.core.internal.binding.model.DataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeEventFilter;
import org.td4j.core.model.IObserver;

import ch.miranet.commons.TK;


public abstract class BaseWidgetController<W> implements IObserver {

	private Caption caption;

	private boolean modelUpdateInProgress;
	private boolean viewUpdateInProgress;

	
	public Caption getCaption() {
		return caption;
	}

	public void setCaption(Caption caption) {
		this.caption = caption;
		updateCaption();
	}
	
	public void observableChanged(ChangeEvent event) {
		updateView();
	}
	
	public String toString() {
		return getClass().getSimpleName() + ": " + getDataProxy().getName();
	}
	
	public abstract W getWidget();	
	public abstract DataProxy getDataProxy();
	
		
	protected void updateCaption() {
		if (caption != null) {
			final String name = TK.Strings.humanize(getDataProxy().getName());
			caption.setText(name);
		}
	}
		
	protected void registerAsObserver(DataProxy dataProxy) {
		dataProxy.addObserver(this, new ChangeEventFilter(dataProxy, ChangeEvent.Type.StateChange));
	}	
	
	protected boolean isViewUpdateInProgress() {
		return viewUpdateInProgress;
	}
	
	protected boolean isModelUpdateInProgress() {
		return modelUpdateInProgress;
	}
	
	protected final void updateView() {
		if (modelUpdateInProgress) return;
		if ( ! canRead())          return;
		if (getWidget() == null)   return; // during construction phase

		setAccess();
		
		viewUpdateInProgress = true;
		try {
			readModelAndUpdateView();
		} finally {
			viewUpdateInProgress = false;
		}
	}
	
	protected abstract void readModelAndUpdateView();

	
	protected final void updateModel() {
		if (viewUpdateInProgress) return;
		if ( ! canWrite())        return;

		modelUpdateInProgress = true;
		try {
			readViewAndUpdateModel();
		} finally {
			modelUpdateInProgress = false;
			
			// refetch the value to display from the model
			updateView();
		}
	}
	
	protected abstract void readViewAndUpdateModel();
	
	protected abstract boolean canWrite();
	protected abstract boolean canRead();	

	protected abstract void setAccess();

}
