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

package org.td4j.core.internal.binding.ui;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.CollectionDataProxy;
import org.td4j.core.binding.model.ICollectionDataConnector;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.reflect.PendingConnectorInfo;
import org.td4j.core.tk.ObjectTK;



public abstract class CollectionControllerFactory<T> {
	private final Mediator mediator;
	private final IDataConnectorFactory conFactory;

	protected CollectionControllerFactory(Mediator mediator, IDataConnectorFactory connectorFactory) {
		this.mediator = ObjectTK.enforceNotNull(mediator, "mediator");
		this.conFactory = ObjectTK.enforceNotNull(connectorFactory, "connectorFactory");
	}

	public T bind(CollectionDataProxy dataProxy) {
		ObjectTK.enforceNotNull(dataProxy, "dataProxy");
		final T controller = createController(dataProxy);
		return controller;
	}

	public T bindConnector(ICollectionDataConnector connector) {
		final CollectionDataProxy proxy = connector.createProxy();
		mediator.addModelSocket(proxy);
		return bind(proxy);
	}

	public T bindField(String fieldName) {
		
		// TODO: handle infoQueue correctly
		final List<PendingConnectorInfo> infoQueue = new ArrayList<PendingConnectorInfo>();
		
		final ICollectionDataConnector connector = conFactory.createCollectionFieldConnector(mediator.getModelType(), fieldName, infoQueue);
		return bindConnector(connector);
	}

	public T bindMethods(String name) {
		
		// TODO: handle infoQueue correctly
		final List<PendingConnectorInfo> infoQueue = new ArrayList<PendingConnectorInfo>();
		
		final ICollectionDataConnector connector = conFactory.createCollectionMethodConnector(mediator.getModelType(), name, infoQueue);
		return bindConnector(connector);
	}

	public T bindMethods(String name, Class<?> argumentType, Object argumentValue) {
		return bindMethods(name, new Class[] { argumentType }, new Object[] { argumentValue });
	}

	public T bindMethods(String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		
		// TODO: handle infoQueue correctly
		final List<PendingConnectorInfo> infoQueue = new ArrayList<PendingConnectorInfo>();		

		final ICollectionDataConnector connector = conFactory.createCollectionMethodConnector(mediator.getModelType(), name, argumentTypes, argumentValues, infoQueue);
		return bindConnector(connector);
	}

	protected abstract T createController(CollectionDataProxy proxy);

}