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

package org.td4j.core.reflect;

import java.util.Collections;
import java.util.List;

import org.td4j.core.binding.model.ListDataConnector;
import org.td4j.core.internal.capability.NestedPropertiesProvider;

import ch.miranet.commons.TK;

public class ListProperty implements ListDataConnector, NestedPropertiesProvider, Property {
	
	private final String name;
	protected final ListDataConnector dataConnector;
	private IndividualProperty[] nestedProperties;
	
	public ListProperty(String name, ListDataConnector dataConnector, IndividualProperty[] nestedProperties) {
		this.name = TK.Strings.assertNotEmpty(name, "name");
		this.dataConnector = TK.Objects.assertNotNull(dataConnector, "dataConnector");
		setNestedProperties(nestedProperties);
	}
	
	protected ListProperty(String name, ListDataConnector dataConnector) {
		this.name = TK.Strings.assertNotEmpty(name, "name");
		this.dataConnector = TK.Objects.assertNotNull(dataConnector, "dataConnector");
	}
	
	public String getName() {
		return name;
	}
		
	public Class<?> getContextType() {
		return dataConnector.getContextType();
	}
	
	public Class<?> getValueType() {
		return dataConnector.getValueType();
	}
	
	public List<?> readValue(Object ctx) {
		TK.Objects.assertNotNull(ctx, "ctx");
		final List<?> values = dataConnector.readValue(ctx);
		
		List<?> result = Collections.emptyList(); 
		if (values != null) {
			result = values; 
		}
		
		return result;
	}

	public boolean canRead() { return dataConnector.canRead(); }
	
	public boolean canRead(Object ctx) {
		if (ctx == null) return false;
		return dataConnector.canRead(ctx);		
	}
	
	protected void setNestedProperties(IndividualProperty[] nestedProperties) {
		this.nestedProperties = TK.Objects.assertNotNull(nestedProperties, "nestedProperties");
	}
	
	public IndividualProperty[] getNestedProperties() {
		return nestedProperties;
	}
	
	public boolean isNestedPropertiesDefined() {
		return nestedProperties.length > 0;
	}	
	
	@Override
	public String toString() {
		return name;
	}
	
}
