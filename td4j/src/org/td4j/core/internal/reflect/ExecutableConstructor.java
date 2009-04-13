/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

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

package org.td4j.core.internal.reflect;

import java.lang.reflect.Constructor;
import java.util.List;

import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;


public class ExecutableConstructor extends AbstractExecutable {

	private final Constructor<?> constructor;
	private final List<InvokationParameter> parameters;

	public ExecutableConstructor(Constructor<?> constructor, String... paramNames) {
		this.constructor = ObjectTK.enforceNotNull(constructor, "constructor");
		if ( ! constructor.isAccessible()) {
			constructor.setAccessible(true);
		}

		parameters = createInvokationParameters(paramNames, constructor.getParameterTypes());
	}

	@Override
	public Object invoke(Object... args) {
		Object instance = null;

		try {
			instance = constructor.newInstance(args);
		} catch (Exception e) {
			// PEND:
			throw new RuntimeException(e);
		}

		return instance;
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public List<InvokationParameter> getParameters() {
		return parameters;
	}
	
	@Override
	public Class getReturnItemType() {
	  return constructor.getDeclaringClass();
	}

	@Override
	public String toString() {
		final String name = constructor.getDeclaringClass().getSimpleName();
		final String paramNames = paramNamesToString(parameters);
		return "+ " + (StringTK.isEmpty(paramNames) ? name : String.format("%1$s: %2$s", name, paramNames));
	}

}
