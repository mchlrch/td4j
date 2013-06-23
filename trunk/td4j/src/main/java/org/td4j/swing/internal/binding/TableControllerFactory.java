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

import javax.swing.JTable;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.Caption;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.internal.binding.ui.ListWidgetControllerFactory;
import org.td4j.core.metamodel.MetaClassProvider;
import org.td4j.swing.binding.TableController;
import org.td4j.swing.workbench.Navigator;

import ch.miranet.commons.container.Option;


public class TableControllerFactory extends ListWidgetControllerFactory<TableController, JTable> {

	private final Option<Navigator> navigator;

	public TableControllerFactory(Mediator<?> mediator, DataConnectorFactory connectorFactory, Option<MetaClassProvider> metaClassProvider, JTable widget, Caption caption, Option<Navigator> navigator) {
		super(mediator, connectorFactory, metaClassProvider, widget, caption);
		this.navigator = navigator;		
	}

	@Override
	protected TableController createController(ListDataProxy dataProxy, JTable widget) {
// should be obsolete by now !?		dataProxy.ensureSensibleNestedProperties(metaModel);
		return new TableController(widget, dataProxy, navigator);
	}	

}
