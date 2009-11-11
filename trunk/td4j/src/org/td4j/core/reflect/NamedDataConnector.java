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

package org.td4j.core.reflect;

import org.td4j.core.binding.model.IDataConnector;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;

class NamedDataConnector {
	
	private final IDataConnector connector;
	private final String name;
	
	NamedDataConnector(IDataConnector connector, String name) {
		this.connector = ObjectTK.enforceNotNull(connector, "connector");
		this.name = StringTK.enforceNotEmpty(name, "name");
	}
	
	public IDataConnector getConnector() {
		return connector;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
