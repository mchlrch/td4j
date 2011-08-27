/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2010 Michael Rauch

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


import ch.miranet.commons.TK;
import ch.miranet.commons.filter.Filter;

public class ObservableTK {

	public static boolean attachObserverToModel(Object model, IObserver observer) {
		return attachObserverToModel(model, observer, null); 
	}
	
	// PEND: prefer this method over the above in framework code to reduce callbacks from observable
	public static boolean attachObserverToModel(Object model, IObserver observer, Filter<ChangeEvent> eventFilter) {
		TK.Objects.assertNotNull(observer, "observer");

		if (model instanceof IObservable) {
			((IObservable) model).addObserver(observer, eventFilter);
			return true;
		}

		return false;
	}

	public static boolean detachObserverFromModel(Object model, IObserver observer) {
		TK.Objects.assertNotNull(observer, "observer");

		if (model instanceof IObservable) {
			((IObservable) model).removeObserver(observer);
			return true;
		}

		return false;
	}

}
