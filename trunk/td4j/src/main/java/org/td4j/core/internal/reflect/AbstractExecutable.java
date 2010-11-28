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

import java.util.ArrayList;
import java.util.List;

import ch.miranet.commons.StringTK;


public abstract class AbstractExecutable {

	protected static List<InvokationParameter> createInvokationParameters(String[] paramNames, Class<?>[] paramTypes) {
		final List<InvokationParameter> parameters = new ArrayList<InvokationParameter>();

		for (int i = 0, n = paramTypes.length; i < n; i++) {
			final Class<?> paramType = paramTypes[i];
			final String paramName = paramNames != null && i < paramNames.length ? paramNames[i] : paramTypes[i].getSimpleName();
			parameters.add(new InvokationParameter(paramName, paramType));
		}

		return parameters;
	}

	protected static String paramNamesToString(List<InvokationParameter> parameters) {
		final StringBuilder paramNames = new StringBuilder();
		for (InvokationParameter param : parameters) {
			if (paramNames.length() > 0) paramNames.append(", ");
			final String paramName = param.getName();
			if ( ! StringTK.isEmpty(paramName)) {
				paramNames.append(paramName);
			} else {
				paramNames.append(param.getType().getSimpleName());
			}
		}
		return paramNames.toString();
	}

	public abstract Object invoke(Object... args);

	public abstract boolean isStatic();

	public abstract List<InvokationParameter> getParameters();
	
	public abstract Class getReturnItemType();

}
