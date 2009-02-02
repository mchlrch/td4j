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

package org.td4j.core.model;

import java.util.HashMap;
import java.util.Map;

import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;



public class CountingObserver implements IObserver {
	public int propertyChangeCount;
	public int stateChangeCount;
	public final Map<String, Object> newValue = new HashMap<String, Object>();

	public void observableChanged(ChangeEvent evt) {
		if (evt.getType() == ChangeEvent.Type.LazyPropertyChange) {
			propertyChangeCount++;
			newValue.clear();

		} else if (evt.getType() == ChangeEvent.Type.PropertyChange) {
			propertyChangeCount++;
			for (String pName : evt.getPropertyNames()) {
				newValue.put(pName, evt.getNewValue(pName));
			}

		} else if (evt.getType() == ChangeEvent.Type.StateChange) {
			stateChangeCount++;
			newValue.clear();
		}
	}
}