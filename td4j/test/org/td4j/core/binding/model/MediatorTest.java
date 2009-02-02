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

import org.td4j.core.binding.Mediator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class MediatorTest {

	@DataProvider(name = "harness")
	public Object[][] createHarness() {
		final Mediator mediator = new Mediator(String.class);
		final String model = "foo";
		mediator.setModel(model);
		final CountingModelSocket socket = new CountingModelSocket(String.class);
		mediator.addModelSocket(socket);
		return new Object[][] { { mediator, model, socket } };
	}

	@Test(dataProvider = "harness")
	public void testModelKeeper(Mediator mediator, Object model, CountingModelSocket socket) {
		assert model == mediator.getModel();
		assert String.class == mediator.getModelType();
	}

	@Test(dataProvider = "harness", expectedExceptions = { IllegalArgumentException.class }, dependsOnMethods = { "testModelKeeper" })
	public void testInvalidModel(Mediator mediator, Object model, CountingModelSocket socket) {
		mediator.setModel(new Integer(1));
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testModelKeeper" })
	public void testModelSocketDelegate(Mediator mediator, Object model, CountingModelSocket socket) {
		assert socket.setModelCount == 1;
		assert socket.getModel() == model;

		mediator.setModel(null);
		assert socket.setModelCount == 2;
		assert socket.getModel() == null;

		final String newModel = "bar";
		mediator.setModel(newModel);
		assert socket.setModelCount == 3;
		assert socket.getModel() == newModel;
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testModelSocketDelegate" })
	public void testModelSocketDelegateRemoval(Mediator mediator, Object model, CountingModelSocket socket) {
		mediator.removeModelSocket(socket);
		assert socket.setModelCount == 2;
		assert socket.getModel() == null;

		mediator.setModel("bar");
		assert socket.setModelCount == 2;
		assert socket.getModel() == null;
	}

	@Test(dataProvider = "harness", dependsOnMethods = { "testModelKeeper" })
	public void testReloadModel(Mediator mediator, Object model, CountingModelSocket socket) {
		assert socket.refreshFromModelCount == 0;

		mediator.refreshFromModel();
		assert socket.refreshFromModelCount == 1;
	}

	@Test(dataProvider = "harness")
	public void testMediatorCascade(Mediator mediator, Object model, CountingModelSocket socket) {
		final Mediator subMediator = new Mediator(String.class);
		final CountingModelSocket subSocket = new CountingModelSocket(String.class);
		subMediator.addModelSocket(subSocket);
		assert subMediator.getModel() == null;
		assert subSocket.getModel() == null;
		assert subSocket.setModelCount == 1;

		mediator.addModelSocket(subMediator);
		assert subMediator.getModel() == model;
		assert subSocket.getModel() == model;
		assert subSocket.setModelCount == 2;
	}

}
