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

import javax.swing.ListSelectionModel;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.internal.binding.ui.ScalarControllerFactory;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.binding.IOrderedElementModel;
import org.td4j.swing.binding.SelectionController;


public class SelectionControllerFactory extends ScalarControllerFactory<SelectionController> {

	private final ListSelectionModel selectionModel;
	private final IOrderedElementModel dataModel;

	public SelectionControllerFactory(Mediator mediator, IDataConnectorFactory connectorFactory, ListSelectionModel selectionModel, IOrderedElementModel dataModel) {
		super(mediator, connectorFactory);

		this.selectionModel = ObjectTK.enforceNotNull(selectionModel, "selectionModel");
		this.dataModel = ObjectTK.enforceNotNull(dataModel, "dataModel");
	}

	@Override
	protected SelectionController createController(ScalarDataProxy dataProxy) {
		return new SelectionController(selectionModel, dataModel, dataProxy);
	}
}
