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

package ch.miranet.vof;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ch.miranet.commons.ArrayTK;
import ch.miranet.commons.filter.Filter;
import ch.miranet.commons.reflect.ReflectionTK;

/**
 * Value Object Factory.
 * Creates generic value objects from interfaces (backed by a map)
 */
public class VOFactory {

	private static final Filter<Method> defaultMethodFilter = new Filter<Method>() {
		public boolean accept(Method m) {
			return ! (m.getDeclaringClass() == Object.class); // filter out Object.getClass()
		};
	};
	

	// PEND: support implementation of multiple interfaces
	// ... and write testcases for overlapping properties!!
	public <T> T createImplementation(Class<T> iface) {
		final Map<PropertySignature, Object> attributePresets = Collections.emptyMap();
		return createImplementation(iface, attributePresets);
	}
	
	
	<T> T createImplementation(Class<T> iface, Map<PropertySignature, Object> attributePresets) {
		final List<PropertySignature> properties = getProperties(iface);
		if (properties.isEmpty()) throw new IllegalArgumentException("Interface " + iface.getName() + " does not define any properties");

		final GenericVOInvocationHandler handler = new GenericVOInvocationHandler(this, properties.toArray(new PropertySignature[properties.size()]), attributePresets);
		final Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] {iface}, handler);
		
		final T result = (T) proxy;
		return result;

	}

	// PEND: signatures pro Klasse cachen
	private List<PropertySignature> getProperties(Class<?> cls) {
		final List<Method> allMethods = ReflectionTK.getMethods(cls, defaultMethodFilter);
		final List<PropertySignature> properties = new ArrayList<PropertySignature>();
		final HashSet<String> propertyNames = new HashSet<String>();
		for (Method m : allMethods) {
			final String pName = getterToPropertyName(m);
			if (pName != null && ! propertyNames.contains(pName)) {
				
				final Method setter = setterForProperty(allMethods, pName, ArrayTK.append(m.getParameterTypes(), m.getReturnType()));				
				final PropertySignature property = new PropertySignature(pName, m.getReturnType(), m.getParameterTypes(), m, setter);
				properties.add(property);
				propertyNames.add(pName);
			}
		}

		return properties;
	}

	/**
	 * Extracts the property name from the given method
	 * 
	 * @return null if the method doesn't belong to a property
	 */
	private String getterToPropertyName(Method m) {
		final Class<?> returnType = m.getReturnType();
		final Class<?>[] paramTypes = m.getParameterTypes();

		// PEND: im moment ignorieren wir map-properties .. diese wollen wir aber
		// auch unterstützen
		if (returnType != null && paramTypes.length == 0) {
			final String name = m.getName();
			if (name.startsWith("get") && name.length() > 3) {
				return ReflectionTK.decapitalize(name.substring(3));

			} else if (name.startsWith("is") && name.length() > 2) {
				if (Boolean.class.isAssignableFrom(returnType) || boolean.class.isAssignableFrom(returnType)) {
					return ReflectionTK.decapitalize(name.substring(2));
				}
			}
		}
		return null;
	}
	
	private Method setterForProperty(List<Method> allMethods, String propertyName, Class<?>[] parameterTypes) {
		for (Method m : allMethods) {
			final String name = m.getName();
			if (name.startsWith("set") && name.length() > 3) {
				final String pName = ReflectionTK.decapitalize(name.substring(3));
				if (propertyName.equals(pName) && m.getReturnType() == void.class) {
					if (Arrays.equals(parameterTypes, m.getParameterTypes())) {
						return m;
					}
				}
			}			
		}
		return null;
	}

}
