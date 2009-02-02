/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

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

import javax.swing.JList;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.CollectionDataProxy;
import org.td4j.core.binding.model.ICaption;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.internal.binding.ui.CollectionWidgetControllerFactory;
import org.td4j.swing.binding.ListController;


public class ListControllerFactory extends CollectionWidgetControllerFactory<ListController, JList> {

	public ListControllerFactory(Mediator mediator, IDataConnectorFactory connectorFactory, JList widget, ICaption caption) {
		super(mediator, connectorFactory, widget, caption);
	}

	@Override
	protected ListController createController(CollectionDataProxy dataProxy, JList widget) {
		return new ListController(widget, dataProxy);
	}

}
