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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


import ch.miranet.commons.ArrayTK;
import ch.miranet.commons.ObjectTK;

// PEND: wie kann equals() und hashCode() implementiert werden?

public class GenericVOInvocationHandler implements InvocationHandler {
	
	private final VOFactory voFactory;
	private final Map<PropertySignature, Object> attributeMap = new HashMap<PropertySignature, Object>();
	private final Map<Method, PropertySignature> propertyMap = new HashMap<Method, PropertySignature>();
	
	GenericVOInvocationHandler(VOFactory voFactory, PropertySignature[] properties, Map<PropertySignature, Object> attributePresets) {
		this.voFactory = ObjectTK.enforceNotNull(voFactory, "voFactory");
		ArrayTK.enforceNotEmpty(properties, "properties");
		for (PropertySignature property : properties) {
			attributeMap.put(property, null);
			propertyMap.put(property.getGetter(), property);
			if (property.isWritable()) {
				propertyMap.put(property.getSetter(), property);
			}
		}
		
		// populate attributeMap with presets
		if ( ! attributePresets.isEmpty()) {
			for (PropertySignature property : attributeMap.keySet()) {
				final Object presetValue = attributePresets.get(property);
				if (presetValue != null) {
					attributeMap.put(property, presetValue);   // PEND: check for deep copy
				}
			}
		}
	}
	
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final PropertySignature property = propertyMap.get(method);
		
		// handle properties
		if (property != null) {		
			// PEND: im moment sind map-properties noch nicht unterstützt
			if (method.equals(property.getGetter())) {
				return attributeMap.get(property);
				
			} else if (method.equals(property.getSetter())) {
				attributeMap.put(property, args[0]);
				return null;
				
			} else {			
				throw new IllegalStateException();
			}
			
			
		// handle clone methods
		} else if ("createmutableclone".equals(method.getName().toLowerCase()) || "createimmutableclone".equals(method.getName().toLowerCase())) {
			final Map<PropertySignature, Object> attributePresets = new HashMap<PropertySignature, Object>(attributeMap); 
			final Object clone = voFactory.createImplementation(method.getReturnType(), attributePresets);
			return clone;			
		
		}	else {
			throw new IllegalStateException("no property for method: " + method);
		}
	}
	
	

}
