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

import org.td4j.core.binding.model.IndividualDataProxy;

import ch.miranet.commons.ObjectTK;


public abstract class IndividualWidgetController<W> extends BaseWidgetController<W> {

	private final IndividualDataProxy dataProxy;

	
	protected IndividualWidgetController(IndividualDataProxy proxy) {
		dataProxy = ObjectTK.enforceNotNull(proxy, "proxy");
		registerAsObserver(proxy);
	}

	public IndividualDataProxy getDataProxy() {
		return dataProxy;
	}
	
	
	protected void readModelAndUpdateView() {
		final Object modelValue = canRead() ? dataProxy.readValue() : null;
		updateView0(modelValue);
	}

	protected void readViewAndUpdateModel() {
		final Object viewValue = readView0();
		dataProxy.writeValue(viewValue);
	}

	protected boolean canWrite() {
		return dataProxy.canWrite();
	}

	protected boolean canRead() {
		return dataProxy.canRead();
	}	

	protected abstract void updateView0(Object newValue);
	protected abstract Object readView0();

}
