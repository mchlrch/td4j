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
import java.util.Arrays;

import ch.miranet.commons.ArrayTK;
import ch.miranet.commons.ObjectTK;



public class IndividualMethodConnector extends AbstractIndividualDataConnector {

	private final Method getterMethod;
	private final Method setterMethod;
	private final Object[] argumentValues;

	public IndividualMethodConnector(Class<?> ctxType, Method getter, Method setter) {
		this(ctxType, getter, setter, new Object[0]);
	}

	public IndividualMethodConnector(Class<?> ctxType, Method getter, Method setter, Object[] argumentValues) {
		super(ctxType, getter.getReturnType());

		this.getterMethod = getter;
		this.setterMethod = setter;
		
		if ( ! getter.isAccessible()) getter.setAccessible(true);
		if (setter != null && ! setter.isAccessible()) setter.setAccessible(true);
		
		this.argumentValues = ObjectTK.enforceNotNull(argumentValues, "argumentValues");
	}
	
	public Method getGetterMethod() {
		return getterMethod;
	}
	
	public Method getSetterMethod() {
		return setterMethod;
	}

	public boolean canRead()  { return getterMethod != null; }	
	public boolean canWrite() { return setterMethod != null; }
	
	public boolean canRead(Object ctx)  { return canRead() && ctx != null;  }
	public boolean canWrite(Object ctx) { return canWrite() && ctx != null; }

	@Override
	protected Object readValue0(Object ctx) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		return getterMethod.invoke(ctx, argumentValues);
	}

	@Override
	protected void writeValue0(Object ctx, Object val) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		setterMethod.invoke(ctx, ArrayTK.append(argumentValues, val));
	}

	@Override
	public String toString() {
		return getContextType().getName() + "." + getterMethod.getName() + ":" + getterMethod.getReturnType().getName();
	}
	
	@Override
	public int hashCode() {
		int hash = 41 * super.hashCode() + getterMethod.hashCode();
		if (setterMethod != null) {
			hash = 41 * hash + setterMethod.hashCode();
		}
		hash = 41 * hash + Arrays.hashCode(argumentValues); 
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof IndividualMethodConnector) {
			final IndividualMethodConnector that = (IndividualMethodConnector) other;
			return super.equals(other)
					&& that.canEqual(this)
					&& this.getterMethod.equals(that.getterMethod)
					&& methodsAreEqualOrBothNull(this.setterMethod, that.setterMethod)
					&& Arrays.equals(this.argumentValues, that.argumentValues);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean canEqual(Object other) {
		return other instanceof IndividualMethodConnector;
	}
	
	private static boolean methodsAreEqualOrBothNull(Method m1, Method m2) {
		return m1 != null ? m1.equals(m2) : m2 == null;
	}

}
