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

package org.td4j.core.internal.binding.model;

import org.td4j.core.binding.model.TestObservable;
import org.td4j.core.binding.model.TestObservable.NotificationMode;
import org.td4j.core.internal.binding.model.DataProxy;
import org.td4j.core.internal.binding.model.ScalarFieldConnector;
import org.td4j.core.model.CountingObserver;
import org.testng.annotations.Test;



public class DataProxyTest {

	@Test
	public void testModelChange() throws Exception {
		final ScalarFieldConnector con = new ScalarFieldConnector(TestObservable.class, TestObservable.class.getDeclaredField("int1"));
		final DataProxy<ScalarFieldConnector> proxy = new DataProxy<ScalarFieldConnector>(con, con.getName()) {
		};
		final CountingObserver observer = new CountingObserver();
		proxy.addObserver(observer);

		assert observer.stateChangeCount == 0;

		final TestObservable observable1 = new TestObservable(NotificationMode.PropertyChange);
		proxy.setModel(observable1);
		assert proxy.getModel() == observable1;
		assert observer.stateChangeCount == 1;

		final TestObservable observable2 = new TestObservable(NotificationMode.PropertyChange);
		proxy.setModel(observable2);
		assert proxy.getModel() == observable2;
		assert observer.stateChangeCount == 2;

		proxy.setModel(null);
		assert proxy.getModel() == null;
		assert observer.stateChangeCount == 3;
	}

	@Test(dependsOnMethods = { "testModelChange" })
	public void testModelStateChange() throws Exception {
		testModelStateOrPropertyChange(NotificationMode.StateChange);
	}

	@Test(dependsOnMethods = { "testModelChange" })
	public void testModelPropertyChange() throws Exception {
		testModelStateOrPropertyChange(NotificationMode.PropertyChange);
	}

	private void testModelStateOrPropertyChange(NotificationMode notificationMode) throws Exception {
		final ScalarFieldConnector con = new ScalarFieldConnector(TestObservable.class, TestObservable.class.getDeclaredField("int1"));
		final DataProxy<ScalarFieldConnector> proxy = new DataProxy<ScalarFieldConnector>(con, con.getName()) {
		};
		final CountingObserver observer = new CountingObserver();
		proxy.addObserver(observer);
		final TestObservable observable = new TestObservable(notificationMode);
		proxy.setModel(observable);

		assert proxy.getModel() == observable;
		assert observer.stateChangeCount == 1;

		observable.setInt1(11);
		assert observer.stateChangeCount == 2;

		observable.setInt1(22);
		assert observer.stateChangeCount == 3;

		proxy.setModel(null);
		assert observer.stateChangeCount == 4;

		observable.setInt1(33);
		assert observer.stateChangeCount == 4;
	}

}
