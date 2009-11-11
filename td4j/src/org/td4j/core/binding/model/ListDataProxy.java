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

package org.td4j.core.binding.model;

import java.util.List;

import org.td4j.core.internal.binding.model.DataProxy;
import org.td4j.core.internal.capability.ListDataAccess;
import org.td4j.core.internal.capability.NestedPropertyProvider;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.tk.ObjectTK;


public class ListDataProxy extends DataProxy implements NestedPropertyProvider {

	private final ListDataAccess dataAccess;
	
	// TODO: cleanup this hack
	private final NestedPropertyProvider nestedPropertyProvider;
	private ScalarProperty[] nestedProperties;
	
	public ListDataProxy(ListDataAccess dataAccess, String name, NestedPropertyProvider nestedPropertyProvider) {
		super(name);
		this.dataAccess = ObjectTK.enforceNotNull(dataAccess, "dataAccess");
		this.nestedPropertyProvider = ObjectTK.enforceNotNull(nestedPropertyProvider, "nestedPropertyProvider");
	}
	
	@Override
	public Class<?> getModelType() {
		return dataAccess.getContextType();
	}
	
	public Class<?> getValueType() {
		return dataAccess.getValueType();
	}

	public boolean canRead() {
		return dataAccess.canRead(getModel());
	}

	public List<?> readValue() {
		return dataAccess.readValue(getModel());
	}
	
	public void setNestedProperties(ScalarProperty[] nestedProperties) {
		this.nestedProperties = nestedProperties;
	}

	@Override
	public boolean isNestedPropertiesDefined() {
		final boolean explicit = nestedProperties != null && nestedProperties.length > 0;
		final boolean thruProvider = nestedPropertyProvider.isNestedPropertiesDefined();
		return explicit || thruProvider;
	}
	
	@Override
	public ScalarProperty[] getNestedProperties() {
		if (nestedProperties != null && nestedProperties.length > 0) {
			return nestedProperties;
		} else {
			return nestedPropertyProvider.getNestedProperties();
		}
	}	

	@Override
	public String toString() {
		return dataAccess.toString();
	}
	
}
