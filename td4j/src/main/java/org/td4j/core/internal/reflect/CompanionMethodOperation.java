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

package org.td4j.core.internal.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.SwingUtilities;

import ch.miranet.commons.TK;


public class CompanionMethodOperation extends AbstractOperation {

	private final Object companion;
	private final Method method;
	private final Class<?> itemType;
	private final Class<?> modelType;
	private final List<InvokationParameter> parameters;
	private final boolean statik;

	
	// TODO die Bedeutung von statik ist, dass kein model context für die invokation gebraucht wird => contextIndependent
	public CompanionMethodOperation(Class<?> modelType, Object companion, Method method, String... paramNames) {
		this.modelType = TK.Objects.assertNotNull(modelType, "modelType");
		
		final boolean statikMethod = Modifier.isStatic(method.getModifiers());		
		if (statikMethod) {
			this.companion = null;
		} else {
			this.companion = TK.Objects.assertNotNull(companion, "companion");
		}		
		
		this.method = TK.Objects.assertNotNull(method, "method");
		if ( ! method.isAccessible()) {
			method.setAccessible(true);
		}

		itemType = TK.Reflection.getItemType(method);

		parameters = createInvokationParameters(paramNames, method.getParameterTypes());
		
		if ( ! parameters.isEmpty()) {
			final Class<?> firstArgType = parameters.get(0).getType();
			statik = ! this.modelType.isAssignableFrom(firstArgType);
		} else {
			statik = true;
		}
		if ( ! statik) parameters.remove(0);
	}

	@Override
	public Object invoke(Object... args) {
		Object result = null;

		try {
			result = method.invoke(companion, args);
		} catch (final Exception e) {
			SwingUtilities.invokeLater(new Runnable() {				
				public void run() {					
					throw new RuntimeException(e);
				}
			});
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
	public Class<?> getReturnItemType() {
		return itemType;
	}

	@Override
	public String toString() {
		final String name = method.getName();
		final String paramNames = paramNamesToString(parameters);
		return TK.Strings.isEmpty(paramNames) ? name : String.format("%1$s: %2$s", name, paramNames);
	}

}
