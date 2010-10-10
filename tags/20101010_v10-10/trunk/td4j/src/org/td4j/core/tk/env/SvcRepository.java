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

package org.td4j.core.tk.env;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.td4j.core.tk.ObjectTK;

public class SvcRepository implements SvcProvider {

	private final Map<Class<?>, Object> implMap = new HashMap<Class<?>, Object>();
	private final Map<Class<?>, Konstruktor> implConstructorMap = new HashMap<Class<?>, Konstruktor>();
	
	@Override
	public <T> T getService(Class<T> svcDef) {
		
		@SuppressWarnings("unchecked")
		T impl = (T) implMap.get(svcDef);
		
		if (impl == null) impl = createByImplConstructor(svcDef);
		
		return impl;
	}
	
	@Override
	public <T> T requireService(Class<T> svcDef) {
		final T svc = getService(svcDef);
		if (svc == null) {
			throw new IllegalStateException("Required Service not available: " + svcDef);
		} else {
			return svc;
		}
	}

	public <T, I extends T> void setSingletonService(Class<T> svcDef, I svcImpl) {
		verifyServiceUndefined(svcDef);
		
		implMap.put(svcDef, svcImpl);
	}

	public <T> void setSingletonService(Class<T> svcDef, Class<? extends T> svcImpl) {
		verifyServiceUndefined(svcDef);
		
		Konstruktor konstruktor = null;
		
		// prefer no-arg constructor over constructor with SvcRepository as argument
		try {
			 final Constructor<? extends T> constructor = svcImpl.getConstructor();
			 konstruktor = new Konstruktor(Konstruktor.ArgType.NoArg, constructor);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			// try constructor with SvcProvider argument
		}
		
		// try constructor with SvcProvider as argument
		if (konstruktor == null) {
			try {
				final Constructor<? extends T> constructor = svcImpl.getConstructor(SvcProvider.class);
				konstruktor = new Konstruktor(Konstruktor.ArgType.SvcProviderArg, constructor);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("No appropriate constructor found in " + svcImpl + " : init() or init(" + SvcProvider.class.getName() + ")");
			}	
		}
		
		implConstructorMap.put(svcDef, konstruktor);
	}
	
	private <T> T createByImplConstructor(Class<T> svcDef) {
		final Konstruktor konstruktor = implConstructorMap.get(svcDef);
		if (konstruktor != null) {
			final T svc = (T) konstruktor.newInstance(this);
			return svc;
		}
		
		return null;
	}	
	
	private void verifyServiceUndefined(Class<?> svcDef) {
		if (implMap.containsKey(svcDef)
				|| implConstructorMap.containsKey(svcDef)) {
			
			throw new IllegalStateException("Service implementation already defined.");
		}
	}
	
	// TODO
	public void clearService(Class<?> svcDef) {		
		throw new UnsupportedOperationException();
	}
	
	// --------------------------------------------------------------
	
	static class Konstruktor {
		private static enum ArgType {NoArg, SvcProviderArg}
		
		private final ArgType argType;
		private final Constructor<?> constructor;
		
		private Konstruktor(ArgType argType, Constructor<?> constructor) {
			this.argType = ObjectTK.enforceNotNull(argType, "argType");
			this.constructor = ObjectTK.enforceNotNull(constructor, "constructor");
		}
		
		@SuppressWarnings("unchecked")
		private <T> T newInstance(SvcProvider repo) {
			try { 
				if (argType == ArgType.NoArg) {
					return (T) constructor.newInstance();
				} else {
					return (T) constructor.newInstance(repo);
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
	
}
