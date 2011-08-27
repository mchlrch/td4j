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

package org.td4j.core.binding.model;

import org.td4j.core.model.Observable;

import ch.miranet.commons.TK;



public class TestObservable extends Observable {

	public static enum NotificationMode {
		StateChange, PropertyChange
	}

	private final NotificationMode mode;

	private int int1;
	private int int2;

	public TestObservable(NotificationMode notificationMode) {
		this.mode = TK.Objects.assertNotNull(notificationMode, "notificationMode");
	}

	public int getInt1() {
		return int1;
	}

	public void setInt1(int i) {
		int1 = i;

		switch (mode) {
		case StateChange:
			changeSupport.fireStateChange();
			break;
		case PropertyChange:
			changeSupport.fireLazyPropertyChange("int1");
			break;
		}
	}

	public int getInt2() {
		return int2;
	}

	public void setInt2(int i) {
		int2 = i;

		switch (mode) {
		case StateChange:
			changeSupport.fireStateChange();
			break;
		case PropertyChange:
			changeSupport.fireLazyPropertyChange("int2");
			break;
		}
	}
}