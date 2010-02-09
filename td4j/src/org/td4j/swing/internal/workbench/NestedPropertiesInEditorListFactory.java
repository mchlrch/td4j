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

import org.td4j.core.internal.capability.DefaultNamedScalarDataAccess;
import org.td4j.core.internal.capability.NamedScalarDataAccess;
import org.td4j.core.reflect.ExposePropertiesInEditorList;
import org.td4j.core.reflect.ModelInspector;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.reflect.UnknownPropertyException;
import org.td4j.core.tk.ObjectTK;

// TODO: rewrite to get nestedProperties form OpenClassRepository
public class NestedPropertiesInEditorListFactory {
	
	private final Class<?> modelType;
	private final ModelInspector modelInspector;
	
	
	protected NestedPropertiesInEditorListFactory(Class<?> modelType, ModelInspector modelInspector) {
		this.modelType = ObjectTK.enforceNotNull(modelType, "modelType");
		this.modelInspector = ObjectTK.enforceNotNull(modelInspector, "modelInspector");		
	}
	
	public NamedScalarDataAccess[] createNestedProperties() {
		final ExposePropertiesInEditorList spec = modelType.getAnnotation(ExposePropertiesInEditorList.class);
		
		// check if specified properties are valid, throw exception otherwise
		final List<ScalarProperty> scalarProps = modelInspector.getScalarProperties(modelType);		
		final Map<String, ScalarProperty> propMap = new HashMap<String, ScalarProperty>();
		for (ScalarProperty prop : scalarProps) {
			propMap.put(prop.getName(), prop);
		}
		
		final List<ScalarProperty> result = new ArrayList<ScalarProperty>();
		if (spec != null) {
			final List<String> invalidColumns = new ArrayList<String>();
			for (String colPropName : spec.value()) {
				final ScalarProperty prop = propMap.get(colPropName);
				if (prop != null) {
					result.add(prop);
				} else {
					invalidColumns.add(colPropName);
				}
			}
			if ( ! invalidColumns.isEmpty()) {
				throw new UnknownPropertyException(modelType, invalidColumns.toArray(new String[invalidColumns.size()])); 
			}
			
		// accept all properties
		} else {
			return DefaultNamedScalarDataAccess.createFromProperties(scalarProps);
		}
		
		return DefaultNamedScalarDataAccess.createFromProperties(result);
	}
	
}
