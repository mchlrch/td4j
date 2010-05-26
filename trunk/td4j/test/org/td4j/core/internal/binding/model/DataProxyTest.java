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

package org.td4j.core.internal.binding.model;

import org.td4j.core.binding.model.TestObservable;
import org.td4j.core.binding.model.TestObservable.NotificationMode;
import org.td4j.core.model.CountingObserver;
import org.testng.annotations.Test;



public class DataProxyTest {

	@Test
	public void testContextChange() throws Exception {
		final DataProxy proxy = new DataProxy("foo") {
			@Override
			public Class<?> getContextType() {
				return TestObservable.class;
			}
		};
		final CountingObserver observer = new CountingObserver();
		proxy.addObserver(observer);

		assert observer.stateChangeCount == 0;

		final TestObservable observable1 = new TestObservable(NotificationMode.PropertyChange);
		proxy.setContext(observable1);
		assert proxy.getContext() == observable1;
		assert observer.stateChangeCount == 1;

		final TestObservable observable2 = new TestObservable(NotificationMode.PropertyChange);
		proxy.setContext(observable2);
		assert proxy.getContext() == observable2;
		assert observer.stateChangeCount == 2;

		proxy.setContext(null);
		assert proxy.getContext() == null;
		assert observer.stateChangeCount == 3;
	}

	@Test(dependsOnMethods = { "testContextChange" })
	public void testContextStateChange() throws Exception {
		testContextStateOrPropertyChange(NotificationMode.StateChange);
	}

	@Test(dependsOnMethods = { "testContextChange" })
	public void testModelPropertyChange() throws Exception {
		testContextStateOrPropertyChange(NotificationMode.PropertyChange);
	}

	private void testContextStateOrPropertyChange(NotificationMode notificationMode) throws Exception {
		final DataProxy proxy = new DataProxy("int1") {
			@Override
			public Class<?> getContextType() {
				return TestObservable.class;
			}
		};
		final CountingObserver observer = new CountingObserver();
		proxy.addObserver(observer);
		final TestObservable observable = new TestObservable(notificationMode);
		proxy.setContext(observable);

		assert proxy.getContext() == observable;
		assert observer.stateChangeCount == 1;

		observable.setInt1(11);
		assert observer.stateChangeCount == 2;

		observable.setInt1(22);
		assert observer.stateChangeCount == 3;

		proxy.setContext(null);
		assert observer.stateChangeCount == 4;

		observable.setInt1(33);
		assert observer.stateChangeCount == 4;
	}

}
