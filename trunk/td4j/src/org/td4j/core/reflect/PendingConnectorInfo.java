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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.td4j.core.binding.model.IDataConnector;
import org.td4j.core.tk.ObjectTK;

// TODO: rename and refactor usage
public class PendingConnectorInfo {
	
	private final IDataConnector connector;
	private final Field field;
	private final Method getter;
	
	public PendingConnectorInfo(IDataConnector connector, Field field) {
		this.connector = ObjectTK.enforceNotNull(connector, "connector");
		this.field = ObjectTK.enforceNotNull(field, "field");
		this.getter = null;
	}
	
	public PendingConnectorInfo(IDataConnector connector, Method getter) {
		this.connector = ObjectTK.enforceNotNull(connector, "connector");
		this.getter = ObjectTK.enforceNotNull(getter, "getter");
		this.field = null;
	}

	
	public IDataConnector getConnector() {
		return connector;
	}
	
	public Field getField() {
		return field;
	}
	
	public Method getGetter() {
		return getter;
	}
	
	public boolean isFieldConnector() {
		return field != null;
	}
	
}
