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



public class ScalarMethodConnector extends AbstractScalarDataConnector {

	private final Method getterMethod;
	private final Method setterMethod;
	private final Object[] argumentValues;

	public ScalarMethodConnector(Class<?> modelType, Method getter, Method setter) {
		this(modelType, getter, setter, new Object[0]);
	}

	public ScalarMethodConnector(Class<?> modelType, Method getter, Method setter, Object[] argumentValues) {
		super(modelType, getter.getReturnType());

		this.getterMethod = getter;
		this.setterMethod = setter;
		this.argumentValues = argumentValues;
	}

	public boolean canRead(Object model) {
		return getterMethod != null && model != null;
	}

	public boolean canWrite(Object model) {
		return setterMethod != null && model != null;
	}

	@Override
	protected Object readValue0(Object model) throws Exception {
		ObjectTK.enforceNotNull(model, "model");
		return getterMethod.invoke(model, argumentValues);
	}

	@Override
	protected void writeValue0(Object model, Object val) throws Exception {
		ObjectTK.enforceNotNull(model, "model");
		setterMethod.invoke(model, ReflectionTK.composeArray(argumentValues, val));
	}

	@Override
	public String toString() {
		return getModelType().getName() + "." + getterMethod.getName() + ":" + getterMethod.getReturnType().getName();
	}

}