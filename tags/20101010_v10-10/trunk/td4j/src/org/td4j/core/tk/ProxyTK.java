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

import java.lang.reflect.Proxy;


public class ProxyTK {

	public static <T> T createProxy(Class<T> iface, Object... delegates) {
		final Object proxy = createProxy(ObjectTK.class.getClassLoader(), new Class<?>[] { iface }, delegates);

		// This is OK, as the return type equals the implemented interface
		@SuppressWarnings("unchecked")
		final T proxyResult = (T) proxy;

		return proxyResult;
	}

	public static Object createProxy(ClassLoader loader, Class<?>[] ifaces, Object... delegates) {
		final DelegateFusionInvocationHandler handler = new DelegateFusionInvocationHandler(ifaces, delegates);
		final Object proxy = Proxy.newProxyInstance(loader, ifaces, handler);
		return proxy;
	}

}
