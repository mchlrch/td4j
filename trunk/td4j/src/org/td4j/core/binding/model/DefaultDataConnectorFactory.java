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

package org.td4j.core.binding.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import org.td4j.core.internal.binding.model.CollectionFieldConnector;
import org.td4j.core.internal.binding.model.CollectionMethodConnector;
import org.td4j.core.internal.binding.model.ScalarFieldConnector;
import org.td4j.core.internal.binding.model.ScalarMethodConnector;
import org.td4j.core.reflect.PendingConnectorInfo;
import org.td4j.core.reflect.ReflectionTK;
import org.td4j.core.reflect.UnknownPropertyException;


public class DefaultDataConnectorFactory implements IDataConnectorFactory {

	public IDataConnector createConnector(Class<?> cls, String name) {
		return createConnector(cls, name, null);
	}	
	@Override
	public IDataConnector createConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue) {
		
		// 1) try methods
		try {
			return createMethodConnector(cls, name, infoQueue);
		} catch (Exception ex) {
			// try fields instead ...
		}

		// 2) try fields
		// this possibly throws exception
		return createFieldConnector(cls, name, infoQueue);
	}
	
	
	public IScalarDataConnector createScalarFieldConnector(Class<?> cls, String name) {
		return createScalarFieldConnector(cls, name, null);
	}
	@Override
	public IScalarDataConnector createScalarFieldConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue) {
		return (IScalarDataConnector) createFieldConnector(cls, name, infoQueue);
	}
	
	public ICollectionDataConnector createCollectionFieldConnector(Class<?> cls, String name) {
		return createCollectionFieldConnector(cls, name, null);
	}
	@Override
	public ICollectionDataConnector createCollectionFieldConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue) {
		return (ICollectionDataConnector) createFieldConnector(cls, name, infoQueue);
	}
	
	public IDataConnector createFieldConnector(Class<?> cls, String name) {
		return createFieldConnector(cls, name, null);
	}
	@Override
	public IDataConnector createFieldConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue) {
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
			final CollectionFieldConnector connector = new CollectionFieldConnector(cls, field, itemType);
			enqueueConnectorInfo(infoQueue, connector, field);
			return connector;
		} else {
			return new ScalarFieldConnector(cls, field);
		}
	}
	
	private void enqueueConnectorInfo(List<PendingConnectorInfo> infoQueue, IDataConnector connector, Field field) {
		if (infoQueue == null) return;
		
		final PendingConnectorInfo info = new PendingConnectorInfo(connector, field);
		infoQueue.add(info);
	}
	
	private void enqueueConnectorInfo(List<PendingConnectorInfo> infoQueue, IDataConnector connector, Method getter) {
		if (infoQueue == null) return;
		
		final PendingConnectorInfo info = new PendingConnectorInfo(connector, getter);
		infoQueue.add(info);
	}

	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name) {
		return createScalarMethodConnector(cls, name, null);
	}
	@Override
	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue) {
		return (IScalarDataConnector) createMethodConnector(cls, name, infoQueue);
	}

	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		return createScalarMethodConnector(cls, name, argumentTypes, argumentValues, null);
	}
	@Override
	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues, List<PendingConnectorInfo> infoQueue) {
		return (IScalarDataConnector) createMethodConnector(cls, name, argumentTypes, argumentValues, infoQueue);
	}
	
	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name) {
		return createCollectionMethodConnector(cls, name, null);
	}
	@Override
	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue) {
		return (ICollectionDataConnector) createMethodConnector(cls, name, infoQueue);
	}

	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		return createCollectionMethodConnector(cls, name, argumentTypes, argumentValues, null);
	}
	@Override
	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues, List<PendingConnectorInfo> infoQueue) {
		return (ICollectionDataConnector) createMethodConnector(cls, name, argumentTypes, argumentValues, infoQueue);
	}
	
	public IDataConnector createMethodConnector(Class<?> cls, String name) {
		return createMethodConnector(cls, name);
	}
	@Override
	public IDataConnector createMethodConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue) {
		return createMethodConnector(cls, name, new Class[0], new Object[0], infoQueue);
	}

	public IDataConnector createMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues) {
		return createMethodConnector(cls, name, argumentTypes, argumentValues, null);
	}
	@Override
	public IDataConnector createMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues, List<PendingConnectorInfo> infoQueue) {
		final Method getter = findReadMethod(cls, name, argumentTypes);
		if (getter == null) throw new UnknownPropertyException(cls, name);
		
		if ( ! Modifier.isPublic(getter.getModifiers())) getter.setAccessible(true);

		final Class<?> valueType = getter.getReturnType();
		Method setter = null;
		final Class<?>[] setterArgTypes = ReflectionTK.composeArray(argumentTypes, valueType);
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
			final CollectionMethodConnector connector = new CollectionMethodConnector(cls, name, getter, itemType, argumentValues); 
			enqueueConnectorInfo(infoQueue, connector, getter);
			return connector;

		} else {
			return new ScalarMethodConnector(cls, name, getter, setter, argumentValues);
		}
	}	
	
	
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
