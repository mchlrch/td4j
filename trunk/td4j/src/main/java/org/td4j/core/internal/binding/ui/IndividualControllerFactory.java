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
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.IndividualDataProxy;

import ch.miranet.commons.TK;


public abstract class IndividualControllerFactory<T> {
	private final Mediator<?> mediator;
	private final DataConnectorFactory conFactory;

	protected IndividualControllerFactory(Mediator<?> mediator, DataConnectorFactory connectorFactory) {
		this.mediator   = TK.Objects.assertNotNull(mediator, "mediator");
		this.conFactory = TK.Objects.assertNotNull(connectorFactory, "connectorFactory");			
	}

	public T bind(IndividualDataProxy dataProxy) {
		TK.Objects.assertNotNull(dataProxy, "dataProxy");
		final T controller = createController(dataProxy);
		return controller;
	}
	
	public T bindConnector(IndividualDataConnector connector, String name) {
		final IndividualDataProxy proxy = new IndividualDataProxy(connector, name);
		mediator.addContextSocket(proxy);
		return bind(proxy);
	}	
	
	public T bindField(String fieldName) {
		final IndividualDataConnector connector = conFactory.createIndividualFieldConnector(mediator.getContextType(), fieldName);
		return bindConnector(connector, fieldName);
	}

	public T bindMethods(String name) {
		final IndividualDataConnector connector = conFactory.createIndividualMethodConnector(mediator.getContextType(), name);
		return bindConnector(connector, name);
	}

	public T bindMethods(String name, Class<?> argumentType, Object argumentValue) {
		return bindMethods(name, new Class[] { argumentType }, new Object[] { argumentValue });
	}

	public T bindMethods(String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		final IndividualDataConnector connector = conFactory.createIndividualMethodConnector(mediator.getContextType(), name, argumentTypes, argumentValues);
		return bindConnector(connector, name);
	}
	
	protected abstract T createController(IndividualDataProxy dataProxy);
}