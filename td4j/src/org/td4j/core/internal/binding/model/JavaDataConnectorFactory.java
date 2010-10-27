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

package org.td4j.core.internal.binding.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.reflect.DataConnector;
import org.td4j.core.reflect.UnknownPropertyException;

import ch.miranet.commons.ArrayTK;
import ch.miranet.commons.reflect.ReflectionTK;

public class JavaDataConnectorFactory implements DataConnectorFactory {
	
	public DataConnector createConnector(Class<?> cls, String name) {
		
		// 1) try methods
		try {
			return createMethodConnector(cls, name);
		} catch (Exception ex) {
			// try fields instead ...
		}

		// 2) try fields
		// this possibly throws exception
		return createFieldConnector(cls, name);
	}
	
	public IndividualFieldConnector createIndividualFieldConnector(Class<?> cls, String name) {
		return (IndividualFieldConnector) createFieldConnector(cls, name);
	}
	
	public ListFieldConnector createListFieldConnector(Class<?> cls, String name) {
		return (ListFieldConnector) createFieldConnector(cls, name);
	}
	
	public DataConnector createFieldConnector(Class<?> cls, String name) {
		Field field = null;
		try {
			field = cls.getField(name);
		} catch (Exception ex) {
			// no public field with given name found
		}

		if (field == null) {
			try {
				field = cls.getDeclaredField(name);
				if ( ! Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
			} catch (Exception ex) {
				throw new UnknownPropertyException(cls, name);
			}
		}

		if (Collection.class.isAssignableFrom(field.getType())) {
			final Class<?> itemType = ReflectionTK.getItemType(field);
			final ListFieldConnector connector = new ListFieldConnector(cls, field, itemType);
			return connector;
		} else if (field.getType().isArray()) {
			final Class<?> itemType = ReflectionTK.getItemType(field);
			final ArrayFieldConnector connector = new ArrayFieldConnector(cls, field, itemType);
			return connector;			
		} else {
			return new IndividualFieldConnector(cls, field);
		}
	}

	public IndividualMethodConnector createIndividualMethodConnector(Class<?> cls, String name) {
		return (IndividualMethodConnector) createMethodConnector(cls, name);
	}
	
	public IndividualMethodConnector createIndividualMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		return (IndividualMethodConnector) createMethodConnector(cls, name, argumentTypes, argumentValues);
	}
	
	public ListMethodConnector createListMethodConnector(Class<?> cls, String name) {
		return (ListMethodConnector) createMethodConnector(cls, name);
	}

	public ListMethodConnector createListMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		return (ListMethodConnector) createMethodConnector(cls, name, argumentTypes, argumentValues);
	}
	
	public DataConnector createMethodConnector(Class<?> cls, String name) {
		return createMethodConnector(cls, name, new Class[0], new Object[0]);
	}

	public DataConnector createMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		final Method getter = findReadMethod(cls, name, argumentTypes);
		if (getter == null) throw new UnknownPropertyException(cls, name);
		
		if ( ! Modifier.isPublic(getter.getModifiers())) getter.setAccessible(true);

		final Class<?> valueType = getter.getReturnType();
		Method setter = null;
		final Class<?>[] setterArgTypes = ArrayTK.append(argumentTypes, valueType);
		try {
			setter = cls.getMethod("set" + ReflectionTK.capitalize(name), setterArgTypes);
		} catch (Exception e) {
			try {
				setter = cls.getDeclaredMethod("set" + ReflectionTK.capitalize(name), setterArgTypes);
			}	catch (Exception e2) {
			}
		}
		if (setter != null && ! Modifier.isPublic(setter.getModifiers())) setter.setAccessible(true);

		if (Collection.class.isAssignableFrom(valueType)) {
			final Class<?> itemType = ReflectionTK.getItemType(getter);
			final ListMethodConnector connector = new ListMethodConnector(cls, getter, itemType, argumentValues); 
			return connector;

		} else if (valueType.isArray()) {
			final Class<?> itemType = ReflectionTK.getItemType(getter);
			final ArrayMethodConnector connector = new ArrayMethodConnector(cls, getter, itemType);
			return connector;
			
		} else {
			return new IndividualMethodConnector(cls, getter, setter, argumentValues);
		}
	}

	// ============================================================================
	
	protected Method findReadMethod(Class<?> cls, String name, Class<?>[] argTypes) {
		name = ReflectionTK.capitalize(name);
		try {
			return cls.getMethod("get" + name, argTypes);
		} catch (Exception e) {
		}
		try {
			return cls.getMethod("is" + name, argTypes);
		} catch (Exception e) {
		}
		try {
			return cls.getDeclaredMethod("get" + name, argTypes);
		} catch (Exception e) {
		}
		try {
			return cls.getDeclaredMethod("is" + name, argTypes);
		} catch (Exception e) {
		}
		return null;
	}

}
