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

import java.util.Arrays;
import java.util.List;

import org.td4j.core.binding.model.ICollectionDataConnector;
import org.td4j.core.internal.capability.DefaultNamedScalarDataAccess;
import org.td4j.core.internal.capability.ListDataAccess;
import org.td4j.core.internal.capability.NamedScalarDataAccess;
import org.td4j.core.internal.capability.NestedScalarDataAccessProvider;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;

public class ListProperty implements ListDataAccess, NestedScalarDataAccessProvider {
	
	private final String name;
	private final ICollectionDataConnector dataConnector;
	private final ScalarProperty[] nestedProperties;
	
	public ListProperty(String name, ICollectionDataConnector dataConnector, ScalarProperty[] nestedProperties) {
		this.name = StringTK.enforceNotEmpty(name, "name");
		this.dataConnector = ObjectTK.enforceNotNull(dataConnector, "dataConnector");
		this.nestedProperties = ObjectTK.enforceNotNull(nestedProperties, "nestedProperties");
	}
	
	public String getName() {
		return name;
	}
	
	public Class<?> getContextType() {
		return dataConnector.getModelType();
	}
	
	public Class<?> getValueType() {
		return dataConnector.getType();
	}
	
	public List<?> readValue(Object ctx) {
		ObjectTK.enforceNotNull(ctx, "ctx");
		return (List<?>) dataConnector.readValue(ctx);
	}

	public boolean canRead(Object ctx) {
		if (ctx == null) return false;
		return dataConnector.canRead(ctx);		
	}
	
	
	public ScalarProperty[] getNestedProperties() {
		return nestedProperties;
	}
	
	public boolean isNestedScalarDataAccessDefined() {
		return nestedProperties.length > 0;
	}
	
	public NamedScalarDataAccess[] getNestedScalarDataAccess() {
		return DefaultNamedScalarDataAccess.createFromProperties(Arrays.asList(nestedProperties));
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	
}
