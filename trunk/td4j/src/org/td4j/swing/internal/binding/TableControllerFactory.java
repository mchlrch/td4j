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

import java.util.List;

import javax.swing.JTable;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.ICaption;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.binding.model.ScalarDataConnector;
import org.td4j.core.internal.binding.model.ToStringConnector;
import org.td4j.core.internal.binding.ui.CollectionWidgetControllerFactory;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.binding.TableController;
import org.td4j.swing.workbench.Navigator;


public class TableControllerFactory extends CollectionWidgetControllerFactory<TableController, JTable> {

	private final Navigator navigator;
	private final MetaModel metaModel;

	public TableControllerFactory(Mediator mediator, DataConnectorFactory connectorFactory, MetaModel metaModel, JTable widget, ICaption caption, Navigator navigator) {
		super(mediator, connectorFactory, widget, caption);
		this.metaModel = ObjectTK.enforceNotNull(metaModel, "metaModel");
		this.navigator = navigator;		
	}

	@Override
	protected TableController createController(ListDataProxy dataProxy, JTable widget) {
		final ScalarProperty[] columnProperties = createColumnProperties(dataProxy);
		return new TableController(widget, dataProxy, columnProperties, navigator);
	}
	
	
	// -----------------------------------------------------------------------
	
	// TODO: this code is not Swing specific and should be refactored to another place
	private ScalarProperty[] createColumnProperties(ListDataProxy proxy) {

		// use nestedProperties from proxy, if available
		if (proxy.isNestedPropertiesDefined()) {
			final ScalarProperty[] nestedProperties = proxy.getNestedProperties();
			return nestedProperties;
			
		// otherwise use all scalar properties
		} else {
			final MetaClass metaClass = metaModel.getMetaClass(proxy.getValueType());
			final List<ScalarProperty> scalarProperties = metaClass.getScalarProperties();
			
			if ( ! scalarProperties.isEmpty()) {
				return scalarProperties.toArray(new ScalarProperty[scalarProperties.size()]);			
				
			// primitive rowTypes have no properties - fallback to toString connector to make sure that the table is not blank
			} else {
				final ScalarDataConnector toStringConnector = new ToStringConnector(proxy.getValueType());	
				return new ScalarProperty[] {new ScalarProperty("toString", toStringConnector)};
			}
		}
	}
	
	

}
