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

package org.td4j.core.internal.binding.ui;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.Caption;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataProxy;

import ch.miranet.commons.TK;



public abstract class IndividualWidgetControllerFactory<T extends IndividualWidgetController<W>, W> extends IndividualControllerFactory<T> {
	private final W widget;
	private final Caption caption;

	protected IndividualWidgetControllerFactory(Mediator<?> mediator, DataConnectorFactory connectorFactory, W widget, Caption caption) {
		super(mediator, connectorFactory);

		this.widget = TK.Objects.assertNotNull(widget, "widget");
		this.caption = caption;
	}

	@Override
	public T bind(IndividualDataProxy dataProxy) {
		final T controller = super.bind(dataProxy);
		controller.setCaption(caption);
		return controller;
	}
	
	@Override
	protected T createController(IndividualDataProxy dataProxy) {
		return createController(dataProxy, widget);
	}
	
	protected abstract T createController(IndividualDataProxy dataProxy, W widget);	

}