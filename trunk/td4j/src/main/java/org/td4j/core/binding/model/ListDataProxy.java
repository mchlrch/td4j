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

package org.td4j.core.binding.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.td4j.core.internal.binding.model.DataProxy;
import org.td4j.core.internal.binding.model.ToStringConnector;
import org.td4j.core.internal.capability.NestedPropertiesProvider;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.core.reflect.IndividualProperty;

import ch.miranet.commons.ObjectTK;


public class ListDataProxy extends DataProxy implements NestedPropertiesProvider {

	private final ListDataAccessAdapter dataAccess;	
	private IndividualProperty[] nestedProperties;
	
	public ListDataProxy(ListDataConnector dataConnector, String name) {
		this(dataConnector, name, null);
	}
	
	public ListDataProxy(ListDataConnector dataConnector, String name, IndividualProperty[] nestedProperties) {
		super(name);		
		this.dataAccess = new ListDataAccessAdapter(dataConnector); 
		setNestedProperties(nestedProperties);
	}
	
	@Override
	public Class<?> getContextType() {
		return dataAccess.getContextType();
	}
	
	public Class<?> getValueType() {
		return dataAccess.getValueType();
	}

	public boolean canRead() {
		return dataAccess.canRead(getContext());
	}

	public List<?> readValue() {
		return dataAccess.readValue(getContext());
	}
	
	public void setNestedProperties(IndividualProperty[] nestedProperties) {
		this.nestedProperties = nestedProperties;
	}

	@Override
	public boolean isNestedPropertiesDefined() {
		return nestedProperties != null && nestedProperties.length > 0;
	}
	
	@Override
	public IndividualProperty[] getNestedProperties() {
		if (nestedProperties != null && nestedProperties.length > 0) {
			return nestedProperties;
		} else {
			return new IndividualProperty[0];
		}
	}
	
	
	public void ensureSensibleNestedProperties(MetaModel metaModel) {

		// if proxy already defines nestedProperties, we are done
		if (isNestedPropertiesDefined()) return;
			
		// otherwise use all individual properties		
		final List<IndividualProperty> individualProperties = individualPropertiesFromMetaModel(metaModel, getValueType());			
			
		if ( ! individualProperties.isEmpty()) {
			setNestedProperties(individualProperties.toArray(new IndividualProperty[individualProperties.size()]));			
			
		// primitive rowTypes have no properties, also lack of MetaModel yields to no properties
		// --> fallback to toString connector to make sure that table doesn't stay blank
		} else {
			final IndividualDataConnector toStringConnector = new ToStringConnector(getValueType());	
			setNestedProperties(new IndividualProperty[] {new IndividualProperty("toString", toStringConnector)});
		}		
	}	
	
	private List<IndividualProperty> individualPropertiesFromMetaModel(MetaModel metaModel, Class<?> cls) {
		
		// it's possible to use the ListDataProxy (or WidgetBuilder) without having a MetaModel
		if (metaModel != null) {
			final MetaClass metaClass = metaModel.getMetaClass(cls);
			return metaClass.getIndividualProperties();
		} else {
			return Collections.emptyList();
		}		
	}
	

	@Override
	public String toString() {
		return dataAccess.toString();
	}
		
	// ==================================================================================
	
	private static class ListDataAccessAdapter {

		private final ListDataConnector connector;
		
		private ListDataAccessAdapter(ListDataConnector connector) {
			this.connector = ObjectTK.enforceNotNull(connector, "connector");
		}
		
		public boolean canRead(Object ctx) {
			return connector.canRead(ctx);
		}

		public Class<?> getContextType() {
			return connector.getContextType();
		}

		public Class<?> getValueType() {
			return connector.getValueType();
		}

		public List<?> readValue(Object ctx) {
			final Collection<?> values = connector.readValue(ctx);
			
			List<Object> result = Collections.emptyList(); 
			if (values != null) {
				result = new ArrayList<Object>(values); 
			}
				
			return result;
		}
		
		@Override
		public String toString() {
			return connector.toString();
		}
		
	}
	
}
