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
import java.util.Collection;

public class ListMethodConnector extends AbstractListDataConnector {

	private final Method getterMethod;
	private final Object[] argumentValues;
	
	public ListMethodConnector(Class<?> contextType, Method getter, Class<?> valueType) {
		this(contextType, getter, valueType, new Object[0]);
	}

	public ListMethodConnector(Class<?> contextType, Method getter, Class<?> valueType, Object[] argumentValues) {
		super(contextType, valueType);

		final Class<?> returnType = getter.getReturnType();
		if ( ! Collection.class.isAssignableFrom(returnType)) throw new IllegalArgumentException("not a collection type: " + returnType);
		
		this.getterMethod = getter;
		this.argumentValues = argumentValues;
	}
	
	public Method getGetterMethod() {
		return getterMethod;
	}

	public boolean canRead(Object ctx) {
		return getterMethod != null && ctx != null;
	}
	
	@Override
	protected Collection<?> readValue0(Object ctx) throws Exception {
		return (Collection<?>) getterMethod.invoke(ctx, argumentValues);
	}
	
	@Override
	public String toString() {
		return getContextType().getName() + "." + getterMethod.getName() + " : List<" + getValueType() + ">";
	}
	
}
