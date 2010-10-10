/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008 Michael Rauch

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

package org.td4j.core.tk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DelegateFusionInvocationHandler implements InvocationHandler {

	private final Map<Method, Object> dispatchMap = new HashMap<Method, Object>();

	DelegateFusionInvocationHandler(Class<?>[] ifaces, Object... delegates) {
		ArrayTK.enforceNotEmpty(ifaces, "ifaces");
		ArrayTK.enforceNotEmpty(delegates, "delegates");

		final List<Object> delegateList = Arrays.asList(delegates);
		final Map<Class<?>, List<Method>> delegateMethodMap = new HashMap<Class<?>, List<Method>>();
		for (Object delegate : delegateList) {
			final Class<?> delegateClass = delegate.getClass();
			if ( ! delegateMethodMap.containsKey(delegateClass)) {
				final List<Method> methodList = Arrays.asList(delegateClass.getMethods());
				delegateMethodMap.put(delegateClass, methodList);
			}
		}

		for (Class<?> iface : ifaces) {
			for (Method ifaceMethod : iface.getMethods()) {
				boolean methodMatched = false;
				for (Iterator<Object> delegateIterator = delegateList.iterator(); ! methodMatched && delegateIterator.hasNext();) {
					final Object delegate = delegateIterator.next();
					final Class<?> delegateClass = delegate.getClass();
					for (Iterator<Method> delegateMethodIterator = delegateMethodMap.get(delegateClass).iterator(); ! methodMatched && delegateMethodIterator.hasNext();) {
						final Method delegateMethod = delegateMethodIterator.next();
						if (isImplementationOf(delegateMethod, ifaceMethod)) {
							dispatchMap.put(ifaceMethod, delegate);
							methodMatched = true;
						}
					}
				}

				if ( ! methodMatched) throw new IllegalStateException("no matching delegate for method: " + ifaceMethod);
			}
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final Object delegate = dispatchMap.get(method);
		if (delegate == null) throw new IllegalStateException("no matching delegate for method: " + method);
		if ( ! method.isAccessible()) method.setAccessible(true);
		return method.invoke(delegate, args);
	}

	private boolean isImplementationOf(Method implementedMethod, Method declaredMethod) {
		final Class<?> implClass = implementedMethod.getDeclaringClass();
		final Class<?> declaringClass = implementedMethod.getDeclaringClass();

		if ( ! declaringClass.isAssignableFrom(implClass)) return false;
		if ( ! declaredMethod.getName().equals(implementedMethod.getName())) return false;
		if ( ! declaredMethod.getReturnType().equals(implementedMethod.getReturnType())) return false;

		/* Avoid unnecessary cloning */
		Class<?>[] params1 = declaredMethod.getParameterTypes();
		Class<?>[] params2 = implementedMethod.getParameterTypes();
		if (params1.length != params2.length) return false;
		for (int i = 0; i < params1.length; i++) {
			if (params1[i] != params2[i]) return false;
		}
		return true;
	}

}
