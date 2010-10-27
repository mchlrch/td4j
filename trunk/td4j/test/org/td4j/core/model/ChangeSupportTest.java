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

import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeSupport;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.miranet.commons.filter.Filter;



public class ChangeSupportTest {

	@DataProvider(name = "harness")
	public Object[][] createHarness() {
		final ChangeSupport cs = new ChangeSupport(this);
		final CountingObserver observer = new CountingObserver();
		cs.addObserver(observer);
		return new Object[][] { { cs, observer } };
	}

	// ======== propertyChange ===========

	@Test(dataProvider = "harness")
	public void testPropertyChange(ChangeSupport cs, CountingObserver observer) {
		assert observer.propertyChangeCount == 0;

		final String pName = "foo";

		cs.fireLazyPropertyChange(pName);
		assert observer.propertyChangeCount == 1;
		assert observer.newValue.get(pName) == null;

		final String newValue = "bar";
		cs.firePropertyChange(pName, null, newValue);
		assert observer.propertyChangeCount == 2;
		assert observer.newValue.get(pName) == newValue;

		cs.firePropertyChange(pName, newValue, null);
		assert observer.propertyChangeCount == 3;
		assert observer.newValue.get(pName) == null;
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testPropertyChange" })
	public void testObserverRemoval(ChangeSupport cs, CountingObserver observer) {
		cs.removeObserver(observer);
		cs.firePropertyChange("foo", null, "bar");
		assert observer.propertyChangeCount == 0;
		assert observer.newValue.isEmpty();
	}

	@Test(dataProvider = "harness")
	public void testPropertyChangeFiltered(ChangeSupport cs, CountingObserver observer) {
		final CountingObserver selectiveObserver = new CountingObserver();
		final Filter<ChangeEvent> filter = new Filter<ChangeEvent>() {
			public boolean accept(ChangeEvent element) {
				return element.contains("foo") || element.contains("bar");
			}
		};
		cs.addObserver(selectiveObserver, filter);
		assert observer.propertyChangeCount == 0;
		assert selectiveObserver.propertyChangeCount == 0;

		final String newValue = "bazzab";
		cs.firePropertyChange("baz", null, newValue);
		assert selectiveObserver.propertyChangeCount == 0;
		assert selectiveObserver.newValue.get("baz") == null;

		cs.firePropertyChange("foo", null, newValue);
		assert selectiveObserver.propertyChangeCount == 1;
		assert selectiveObserver.newValue.get("foo") == newValue;

		cs.firePropertyChange("bar", newValue, null);
		assert selectiveObserver.propertyChangeCount == 2;
		assert selectiveObserver.newValue.get("bar") == null;

		assert observer.propertyChangeCount == 3;
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testPropertyChangeFiltered" })
	public void testObserverRemovalFiltered(ChangeSupport cs, CountingObserver observer) {
		final CountingObserver selectiveObserver = new CountingObserver();
		final Filter<ChangeEvent> filter = new Filter<ChangeEvent>() {
			public boolean accept(ChangeEvent element) {
				return element.contains("foo") || element.contains("bar");
			}
		};
		cs.addObserver(selectiveObserver, filter);

		cs.removeObserver(selectiveObserver);

		cs.firePropertyChange("foo", null, "bazzab");
		assert selectiveObserver.propertyChangeCount == 0;
		assert selectiveObserver.newValue.get("foo") == null;
	}

	// ======== stateChange ===========

	@Test(dataProvider = "harness")
	public void testStateChange(ChangeSupport cs, CountingObserver observer) {
		assert observer.stateChangeCount == 0;

		cs.fireStateChange();
		assert observer.stateChangeCount == 1;
		cs.fireStateChange();
		assert observer.stateChangeCount == 2;
	}

	@Test(dataProvider = "harness")
	public void testOberverRemovalStateChange(ChangeSupport cs, CountingObserver observer) {
		cs.removeObserver(observer);
		cs.fireStateChange();
		assert observer.stateChangeCount == 0;
	}

}
