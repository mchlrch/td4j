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

import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.internal.capability.ScalarDataAccess;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;

public class ScalarProperty implements ScalarDataAccess {
	
	private final String name;
	private final IScalarDataConnector dataConnector;
	
	public ScalarProperty(String name, IScalarDataConnector dataConnector) {
		this.name = StringTK.enforceNotEmpty(name, "name");
		this.dataConnector = ObjectTK.enforceNotNull(dataConnector, "dataConnector");
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
	
	// PEND: is context better than model? if yes, change dataConnector sourcecode
	public Object readValue(Object ctx) {
		if (ctx == null) throw new NullPointerException("ctx");
		return dataConnector.readValue(ctx);
	}

	public void writeValue(Object ctx, Object val) {
		if (ctx == null) throw new NullPointerException("ctx");
		dataConnector.writeValue(ctx, val);
	}

	public boolean canRead(Object ctx) {
		if (ctx == null) return false;
		return dataConnector.canRead(ctx);		
	}

	public boolean canWrite(Object ctx) {
		if (ctx == null) return false;
		return dataConnector.canWrite(ctx);
	}	

	@Override
	public String toString() {
		return name;
	}
	
}
