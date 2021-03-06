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

package org.td4j.swing.internal.binding;

import javax.swing.AbstractButton;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.Caption;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.internal.binding.ui.IndividualWidgetControllerFactory;
import org.td4j.swing.binding.ButtonController;


public class ButtonControllerFactory extends IndividualWidgetControllerFactory<ButtonController, AbstractButton> {

	public ButtonControllerFactory(Mediator<?> mediator, DataConnectorFactory connectorFactory, AbstractButton widget, Caption caption) {
		super(mediator, connectorFactory, widget, caption);
	}

	@Override
	protected ButtonController createController(IndividualDataProxy dataProxy, AbstractButton widget) {
		return new ButtonController(widget, dataProxy);
	}

}
