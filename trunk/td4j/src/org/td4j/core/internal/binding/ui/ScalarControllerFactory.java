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

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.ScalarDataConnector;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.tk.ObjectTK;


public abstract class ScalarControllerFactory<T> {
	private final Mediator mediator;
	private final DataConnectorFactory conFactory;

	protected ScalarControllerFactory(Mediator mediator, DataConnectorFactory connectorFactory) {
		this.mediator   = ObjectTK.enforceNotNull(mediator, "mediator");
		this.conFactory = ObjectTK.enforceNotNull(connectorFactory, "connectorFactory");			
	}

	public T bind(ScalarDataProxy dataProxy) {
		ObjectTK.enforceNotNull(dataProxy, "dataProxy");
		final T controller = createController(dataProxy);
		return controller;
	}
	
	public T bindConnector(ScalarDataConnector connector, String name) {
		final ScalarDataProxy proxy = new ScalarDataProxy(connector, name);
		mediator.addModelSocket(proxy);
		return bind(proxy);
	}	
	
	public T bindField(String fieldName) {
		final ScalarDataConnector connector = conFactory.createScalarFieldConnector(mediator.getModelType(), fieldName);
		return bindConnector(connector, fieldName);
	}

	public T bindMethods(String name) {
		final ScalarDataConnector connector = conFactory.createScalarMethodConnector(mediator.getModelType(), name);
		return bindConnector(connector, name);
	}

	public T bindMethods(String name, Class<?> argumentType, Object argumentValue) {
		return bindMethods(name, new Class[] { argumentType }, new Object[] { argumentValue });
	}

	public T bindMethods(String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		final ScalarDataConnector connector = conFactory.createScalarMethodConnector(mediator.getModelType(), name, argumentTypes, argumentValues);
		return bindConnector(connector, name);
	}
	
	protected abstract T createController(ScalarDataProxy dataProxy);
}