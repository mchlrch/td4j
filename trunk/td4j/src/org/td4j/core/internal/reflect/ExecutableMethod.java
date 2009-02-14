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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.td4j.core.reflect.ReflectionTK;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;


public class ExecutableMethod extends AbstractExecutable {

	private final Method method;
	private final Class<?> itemType;
	private final boolean statik;
	private final List<InvokationParameter> parameters;

	public ExecutableMethod(Method method, String... paramNames) {
		this.method = ObjectTK.enforceNotNull(method, "method");
		if ( ! method.isAccessible()) {
			method.setAccessible(true);
		}

		itemType = ReflectionTK.getItemType(method);
		statik = Modifier.isStatic(method.getModifiers());

		parameters = createInvokationParameters(paramNames, method.getParameterTypes());
	}

	@Override
	public Object invoke(Object... args) {
		Object target = null;
		if ( ! Modifier.isStatic(method.getModifiers())) {
			target = args[0];
			args = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new Object[0];
		}
		Object result = null;

		try {
			result = method.invoke(target, args);
		} catch (Exception e) {
			// PEND: exception handling policy
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public boolean isStatic() {
		return statik;
	}

	@Override
	public List<InvokationParameter> getParameters() {
		return parameters;
	}

	@Override
	public Class getReturnItemType() {
		return itemType;
	}

	@Override
	public String toString() {
		final String name = method.getName();
		final String paramNames = paramNamesToString(parameters);
		return StringTK.isEmpty(paramNames) ? name : String.format("%1$s: %2$s", name, paramNames);
	}

}
