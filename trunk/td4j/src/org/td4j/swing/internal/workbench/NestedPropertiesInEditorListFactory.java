/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009 Michael Rauch

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

package org.td4j.swing.internal.workbench;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.td4j.core.binding.model.IDataConnector;
import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.internal.capability.ScalarDataAccess;
import org.td4j.core.internal.capability.ScalarDataAccessAdapter;
import org.td4j.core.reflect.ExposePropertiesInEditorList;
import org.td4j.core.reflect.ModelInspector;
import org.td4j.core.reflect.UnknownPropertyException;
import org.td4j.core.tk.IFilter;
import org.td4j.core.tk.ObjectTK;

public class NestedPropertiesInEditorListFactory {
	
	private static final IFilter<IDataConnector> scalarPlugFilter = new IFilter<IDataConnector>() {
		public boolean accept(IDataConnector element) {
			return IScalarDataConnector.class.isAssignableFrom(element.getClass());
		}
	};
	
	private final Class<?> modelType;
	private final ModelInspector modelInspector;
	
	
	protected NestedPropertiesInEditorListFactory(Class<?> modelType, ModelInspector modelInspector) {
		this.modelType = ObjectTK.enforceNotNull(modelType, "modelType");
		this.modelInspector = ObjectTK.enforceNotNull(modelInspector, "modelInspector");		
	}
	
	public ScalarDataAccess[] createNestedProperties() {
		final ExposePropertiesInEditorList spec = modelType.getAnnotation(ExposePropertiesInEditorList.class);
		
		// check if specified properties are valid, throw exception otherwise
		final List<IDataConnector> allConnectors = modelInspector.getConnectors(modelType, scalarPlugFilter);
		final Map<String, IDataConnector> allConnectorsMap = new HashMap<String, IDataConnector>();
		for (IDataConnector connector : allConnectors) {
			allConnectorsMap.put(connector.getName(), connector);
		}
		
		final List<ScalarDataAccess> result = new ArrayList<ScalarDataAccess>();
		if (spec != null) {
			final List<String> invalidColumns = new ArrayList<String>();
			for (String colPropName : spec.value()) {
				final IDataConnector connector = allConnectorsMap.get(colPropName);
				if (connector instanceof IScalarDataConnector) {
					result.add(new ScalarDataAccessAdapter( (IScalarDataConnector) connector));
				} else {
					invalidColumns.add(colPropName);
				}
			}
			if ( ! invalidColumns.isEmpty()) {
				throw new UnknownPropertyException(modelType, invalidColumns.toArray(new String[invalidColumns.size()])); 
			}
			
		// accept all properties
		} else {
			for (IDataConnector connector : allConnectors) {
				result.add(new ScalarDataAccessAdapter( (IScalarDataConnector) connector));
			}
		}
		
		return result.toArray(new ScalarDataAccess[result.size()]);
	}
	
}
