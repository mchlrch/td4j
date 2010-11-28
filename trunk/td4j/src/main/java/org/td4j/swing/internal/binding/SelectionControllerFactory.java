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

import javax.swing.ListSelectionModel;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.internal.binding.ui.IndividualControllerFactory;
import org.td4j.swing.binding.OrderedElementModel;
import org.td4j.swing.binding.SelectionController;
import org.td4j.swing.binding.SelectionWidget;

import ch.miranet.commons.ObjectTK;


public class SelectionControllerFactory extends IndividualControllerFactory<SelectionController> {

	private final ListSelectionModel selectionModel;
	private final OrderedElementModel dataModel;
	private final SelectionWidget selectionWidget;

	public SelectionControllerFactory(Mediator mediator, DataConnectorFactory connectorFactory, ListSelectionModel selectionModel, OrderedElementModel dataModel, SelectionWidget selectionWidget) {
		super(mediator, connectorFactory);

		this.selectionModel = ObjectTK.enforceNotNull(selectionModel, "selectionModel");
		this.dataModel = ObjectTK.enforceNotNull(dataModel, "dataModel");
		this.selectionWidget = ObjectTK.enforceNotNull(selectionWidget, "selectionWidget");
	}

	@Override
	protected SelectionController createController(IndividualDataProxy dataProxy) {
		return new SelectionController(selectionModel, dataModel, dataProxy, selectionWidget);
	}
}
