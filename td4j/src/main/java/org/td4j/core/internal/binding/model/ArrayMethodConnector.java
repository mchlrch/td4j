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
import java.util.Arrays;
import java.util.Collection;


public class ArrayMethodConnector extends AbstractListMethodConnector {

	public ArrayMethodConnector(Class<?> contextType, Method getter, Class<?> valueType) {
		this(contextType, getter, valueType, new Object[0]);
	}
	
	public ArrayMethodConnector(Class<?> contextType, Method getter, Class<?> valueType, Object[] argumentValues) {
		super(contextType, getter, valueType, argumentValues);

		if ( ! getter.getReturnType().isArray()) throw new IllegalArgumentException("not an array type: " + getter.getReturnType());
	}
	
	public String toString() {
		return getContextType().getName() + "." + getGetterMethod().getName() + " : " + getValueType() + "[]";
	}
	
	@Override
	public boolean canEqual(Object other) {
		return other instanceof ArrayMethodConnector;
	}
	
	@Override
	protected Collection<?> readValue0(Object ctx) throws Exception {
		final Object[] value = (Object[]) readFromGetter(ctx);
		return Arrays.asList(value);
	}	

}
