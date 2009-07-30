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

package org.td4j.swing.binding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.td4j.core.binding.model.IDataConnector;
import org.td4j.core.tk.ArrayTK;
import org.td4j.core.tk.IFilter;

public class TableColumnPropertyFilter implements IFilter<IDataConnector> {
		
	private final Set<String> propertyNames;
	
	protected TableColumnPropertyFilter(String... columnPropertyNames) {
		ArrayTK.enforceNotEmpty(columnPropertyNames, "columnPropertyNames");
		
		propertyNames = new HashSet<String>();
		propertyNames.addAll(Arrays.asList(columnPropertyNames));
	}
	
	@Override
	public boolean accept(IDataConnector connector) {
		return propertyNames.contains(connector.getName());
	}

}
