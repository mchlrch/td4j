/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009, 2010 Michael Rauch

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

import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.reflect.ShowPropertiesInEditorList;
import org.td4j.core.reflect.UnknownPropertyException;

import ch.miranet.commons.TK;

// TODO: rewrite to get nestedProperties form OpenClassRepository
public class NestedPropertiesInEditorListFactory {
	
	private final Class<?> modelType;
	private final MetaModel metaModel;
	
	
	protected NestedPropertiesInEditorListFactory(Class<?> modelType, MetaModel model) {
		this.modelType = TK.Objects.assertNotNull(modelType, "modelType");
		this.metaModel = TK.Objects.assertNotNull(model, "model");		
	}
	
	public IndividualProperty[] createNestedProperties() {
		final ShowPropertiesInEditorList spec = modelType.getAnnotation(ShowPropertiesInEditorList.class);
		
		// check if specified properties are valid, throw exception otherwise
		final MetaClass metaClass = metaModel.getMetaClass(modelType);
		final List<IndividualProperty> individualProps = metaClass.getIndividualProperties();		
		final Map<String, IndividualProperty> propMap = new HashMap<String, IndividualProperty>();
		for (IndividualProperty prop : individualProps) {
			propMap.put(prop.getName(), prop);
		}
		
		final List<IndividualProperty> result = new ArrayList<IndividualProperty>();
		if (spec != null) {
			final List<String> invalidColumns = new ArrayList<String>();
			for (String colPropName : spec.value()) {
				final IndividualProperty prop = propMap.get(colPropName);
				if (prop != null) {
					result.add(prop);
				} else {
					invalidColumns.add(colPropName);
				}
			}
			if ( ! invalidColumns.isEmpty()) {
				throw new UnknownPropertyException(modelType, invalidColumns.toArray(new String[invalidColumns.size()])); 
			}
			
			return result.toArray(new IndividualProperty[result.size()]);
			
		// accept all properties
		} else {
			return individualProps.toArray(new IndividualProperty[individualProps.size()]);
		}		
	}
	
}
