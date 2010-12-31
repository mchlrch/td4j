/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2010 Michael Rauch

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

package org.td4j.core.internal.binding.model;

import java.lang.reflect.Method;


public abstract class AbstractListMethodConnector extends AbstractListDataConnector {

	private final MethodConnectorParts parts;

	
	public AbstractListMethodConnector(Class<?> contextType, Method getter, Class<?> valueType, Object[] argumentValues) {
		super(contextType, valueType);

		this.parts = new MethodConnectorParts(getter, null, argumentValues);		
	}
	
	public Method   getGetterMethod()   { return parts.getGetterMethod(); }
	public Object[] getArgumentValues() { return parts.getArgumentValues(); }

	public boolean canRead()  { return parts.canRead();  }	
	
	public boolean canRead(Object ctx) { return canRead() && (ctx != null || parts.isGetterStatic());	}
	
	public int hashCode() { return parts.hashCode(); }
	
	public boolean equals(Object other) {
		if (other instanceof AbstractListMethodConnector) {
			final AbstractListMethodConnector that = (AbstractListMethodConnector) other;
			return super.equals(other)
					&& that.canEqual(this)
					&& this.parts.equals(that.parts);
		} else {
			return false;
		}
	}
	
	protected Object readFromGetter(Object ctx) throws Exception {
		return parts.readFromGetter(ctx);
	}

}
