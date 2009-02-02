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

import javax.swing.JTable;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.CollectionDataProxy;
import org.td4j.core.binding.model.ICaption;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.internal.binding.ui.CollectionWidgetControllerFactory;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.binding.TableController;
import org.td4j.swing.workbench.Navigator;


public class TableControllerFactory extends CollectionWidgetControllerFactory<TableController, JTable> {

	private final IDataConnectorFactory connectorFactory;
	private final Navigator navigator;

	public TableControllerFactory(Mediator mediator, IDataConnectorFactory connectorFactory, JTable widget, ICaption caption, Navigator navigator) {
		super(mediator, connectorFactory, widget, caption);
		this.connectorFactory = ObjectTK.enforceNotNull(connectorFactory, "connectorFactory");
		this.navigator = ObjectTK.enforceNotNull(navigator, "navigator");
	}

	@Override
	protected TableController createController(CollectionDataProxy dataProxy, JTable widget) {
		return new TableController(widget, dataProxy, connectorFactory, navigator);
	}

}
