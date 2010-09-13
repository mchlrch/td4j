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

import org.td4j.core.reflect.ReflectionTK;
import org.td4j.core.tk.ObjectTK;



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
		
		if (getter != null && ! getter.isAccessible()) getter.setAccessible(true);
		if (setter != null && ! setter.isAccessible()) setter.setAccessible(true);
		
		this.argumentValues = argumentValues;
	}
	
	public Method getGetterMethod() {
		return getterMethod;
	}
	
	public Method getSetterMethod() {
		return setterMethod;
	}

	public boolean canRead(Object ctx) {
		return getterMethod != null && ctx != null;
	}

	public boolean canWrite(Object ctx) {
		return setterMethod != null && ctx != null;
	}

	@Override
	protected Object readValue0(Object ctx) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		return getterMethod.invoke(ctx, argumentValues);
	}

	@Override
	protected void writeValue0(Object ctx, Object val) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		setterMethod.invoke(ctx, ReflectionTK.composeArray(argumentValues, val));
	}

	@Override
	public String toString() {
		return getContextType().getName() + "." + getterMethod.getName() + ":" + getterMethod.getReturnType().getName();
	}

}
