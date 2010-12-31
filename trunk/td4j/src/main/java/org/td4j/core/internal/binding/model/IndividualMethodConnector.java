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

package org.td4j.core.internal.binding.model;

import java.lang.reflect.Method;


public class IndividualMethodConnector extends AbstractIndividualDataConnector {

	private final MethodConnectorParts parts;

	public IndividualMethodConnector(Class<?> ctxType, Method getter, Method setter) {
		this(ctxType, getter, setter, new Object[0]);
	}

	public IndividualMethodConnector(Class<?> ctxType, Method getter, Method setter, Object[] argumentValues) {
		super(ctxType, getter.getReturnType());

		this.parts = new MethodConnectorParts(getter, setter, argumentValues);
	}
	
	public Method   getGetterMethod()   { return parts.getGetterMethod(); }
	public Method   getSetterMethod()   { return parts.getSetterMethod(); }
	public Object[] getArgumentValues() { return parts.getArgumentValues(); }

	public boolean canRead()  { return parts.canRead();  }	
	public boolean canWrite() { return parts.canWrite(); }
	
	public boolean canRead(Object ctx)  { return canRead()  && (ctx != null || parts.isGetterStatic()); }
	public boolean canWrite(Object ctx) { return canWrite() && (ctx != null || parts.isSetterStatic()); }
	
	public String toString() {
		return getContextType().getName() + "." + parts.toString();
	}
	
	public int hashCode() {	return 41 * super.hashCode() + parts.hashCode(); }
	
	public boolean equals(Object other) {
		if (other instanceof IndividualMethodConnector) {
			final IndividualMethodConnector that = (IndividualMethodConnector) other;
			return super.equals(other)
			&& that.canEqual(this)
			&& this.parts.equals(that.parts);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean canEqual(Object other) {
		return other instanceof IndividualMethodConnector;
	}

	
	@Override
	protected Object readValue0(Object ctx) throws Exception {
		return parts.readFromGetter(ctx);
	}

	@Override
	protected void writeValue0(Object ctx, Object val) throws Exception {
		parts.writeToSetter(ctx, val);
	}

}
