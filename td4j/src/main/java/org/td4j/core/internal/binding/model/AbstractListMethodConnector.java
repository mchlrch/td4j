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

import ch.miranet.commons.ObjectTK;


public abstract class AbstractListMethodConnector extends AbstractListDataConnector {

	private final Method getterMethod;
	private final Object[] argumentValues;

	public AbstractListMethodConnector(Class<?> contextType, Method getter, Class<?> valueType, Object[] argumentValues) {
		super(contextType, valueType);

		this.getterMethod = ObjectTK.enforceNotNull(getter, "getter");
		if ( ! getter.isAccessible()) getter.setAccessible(true);
		
		this.argumentValues = ObjectTK.enforceNotNull(argumentValues, "argumentValues");
	}
	
	public Method getGetterMethod() {
		return getterMethod;
	}
	
	public Object[] getArgumentValues() {
		return argumentValues;
	}

	public boolean canRead()  { return getterMethod != null; }
	
	public boolean canRead(Object ctx) {
		return canRead() && (ctx != null || Modifier.isStatic(getterMethod.getModifiers()));
	}
	
	@Override
	public int hashCode() {
		int hash = 41 * super.hashCode() + getterMethod.hashCode();
		hash = 41 * hash + Arrays.hashCode(argumentValues); 
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractListMethodConnector) {
			final AbstractListMethodConnector that = (AbstractListMethodConnector) other;
			return super.equals(other)
					&& that.canEqual(this)
					&& this.getterMethod.equals(that.getterMethod)
					&& Arrays.equals(this.argumentValues, that.argumentValues);
		} else {
			return false;
		}
	}

}
