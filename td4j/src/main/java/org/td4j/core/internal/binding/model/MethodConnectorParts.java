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
import java.lang.reflect.Modifier;
import java.util.Arrays;

import ch.miranet.commons.ArrayTK;
import ch.miranet.commons.ObjectTK;


public class MethodConnectorParts {
	
	private final Method getterMethod;
	private final Method setterMethod;
	private final Object[] argumentValues;

	
	public MethodConnectorParts(Method getter, Method setter, Object[] argumentValues) {
		this.getterMethod = getter;
		this.setterMethod = setter;
		
		if ( ! getter.isAccessible()) getter.setAccessible(true);
		if (setter != null && ! setter.isAccessible()) setter.setAccessible(true);
		
		this.argumentValues = ObjectTK.enforceNotNull(argumentValues, "argumentValues");
	}
	
	public Method   getGetterMethod()   { return getterMethod; }	
	public Method   getSetterMethod()   { return setterMethod; }
	public Object[] getArgumentValues() { return argumentValues; }

	public boolean canRead()  { return getterMethod != null; }	
	public boolean canWrite() { return setterMethod != null; }

	public boolean isGetterStatic() { return Modifier.isStatic(getterMethod.getModifiers()); }
	public boolean isSetterStatic() { return Modifier.isStatic(setterMethod.getModifiers()); }
	
	public Object readFromGetter(Object ctx) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		return getterMethod.invoke(ctx, argumentValues);
	}

	public void writeToSetter(Object ctx, Object val) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		setterMethod.invoke(ctx, ArrayTK.append(argumentValues, val));
	}

	public String toString() {
		return getterMethod.getName() + ":" + getterMethod.getReturnType().getName();
	}
	
	public int hashCode() {
		int hash = getterMethod.hashCode();
		if (setterMethod != null) {
			hash = 41 * hash + setterMethod.hashCode();
		}
		hash = 41 * hash + Arrays.hashCode(argumentValues); 
		return hash;
	}
	
	public boolean equals(Object other) {
		if (other instanceof MethodConnectorParts) {
			final MethodConnectorParts that = (MethodConnectorParts) other;
			return that.canEqual(this)
					&& this.getterMethod.equals(that.getterMethod)
					&& methodsAreEqualOrBothNull(this.setterMethod, that.setterMethod)
					&& Arrays.equals(this.argumentValues, that.argumentValues);
		} else {
			return false;
		}
	}
	
	public boolean canEqual(Object other) {
		return other instanceof MethodConnectorParts;
	}
	
	
	private static boolean methodsAreEqualOrBothNull(Method m1, Method m2) {
		return m1 != null ? m1.equals(m2) : m2 == null;
	}

}
