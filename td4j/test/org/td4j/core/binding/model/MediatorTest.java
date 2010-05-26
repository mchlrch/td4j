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

package org.td4j.core.binding.model;

import org.td4j.core.binding.Mediator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class MediatorTest {

	@DataProvider(name = "harness")
	public Object[][] createHarness() {
		final Mediator<String> mediator = new Mediator<String>(String.class);
		final String model = "foo";
		mediator.setContext(model);
		final CountingModelSocket socket = new CountingModelSocket(String.class);
		mediator.addContextSocket(socket);
		return new Object[][] { { mediator, model, socket } };
	}

	@Test(dataProvider = "harness")
	public void testModelKeeper(Mediator mediator, Object model, CountingModelSocket socket) {
		assert model == mediator.getContext();
		assert String.class == mediator.getContextType();
	}

	@Test(dataProvider = "harness", expectedExceptions = { IllegalArgumentException.class }, dependsOnMethods = { "testModelKeeper" })
	public void testInvalidModel(Mediator mediator, Object model, CountingModelSocket socket) {
		mediator.setContext(new Integer(1));
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testModelKeeper" })
	public void testModelSocketDelegate(Mediator mediator, Object model, CountingModelSocket socket) {
		assert socket.setContextCount == 1;
		assert socket.getContext() == model;

		mediator.setContext(null);
		assert socket.setContextCount == 2;
		assert socket.getContext() == null;

		final String newModel = "bar";
		mediator.setContext(newModel);
		assert socket.setContextCount == 3;
		assert socket.getContext() == newModel;
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testModelSocketDelegate" })
	public void testModelSocketDelegateRemoval(Mediator mediator, Object model, CountingModelSocket socket) {
		mediator.removeContextSocket(socket);
		assert socket.setContextCount == 2;
		assert socket.getContext() == null;

		mediator.setContext("bar");
		assert socket.setContextCount == 2;
		assert socket.getContext() == null;
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testModelKeeper" })
	public void testReloadModel(Mediator mediator, Object model, CountingModelSocket socket) {
		assert socket.refreshFromContextCount == 0;

		mediator.refreshFromContext();
		assert socket.refreshFromContextCount == 1;
	}

	@Test(dataProvider = "harness")
	public void testMediatorCascade(Mediator mediator, Object model, CountingModelSocket socket) {
		final Mediator subMediator = new Mediator(String.class);
		final CountingModelSocket subSocket = new CountingModelSocket(String.class);
		subMediator.addContextSocket(subSocket);
		assert subMediator.getContext() == null;
		assert subSocket.getContext() == null;
		assert subSocket.setContextCount == 1;

		mediator.addContextSocket(subMediator);
		assert subMediator.getContext() == model;
		assert subSocket.getContext() == model;
		assert subSocket.setContextCount == 2;
	}

}
