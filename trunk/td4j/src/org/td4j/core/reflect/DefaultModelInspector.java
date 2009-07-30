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

package org.td4j.core.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.td4j.core.binding.model.ConnectorInfo;
import org.td4j.core.binding.model.IDataConnector;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.internal.reflect.AbstractExecutable;
import org.td4j.core.internal.reflect.ExecutableConstructor;
import org.td4j.core.internal.reflect.ExecutableMethod;
import org.td4j.core.tk.IFilter;
import org.td4j.core.tk.ObjectTK;



public class DefaultModelInspector extends ModelInspector {

	public static final Level DEFAULT_LEVEL = Level.PUBLIC;


	public static enum Level {
		/**
		 * public methods & all fields
		 */
		XRAY,

		/**
		 * public methods & public fields
		 */
		PUBLIC
	};

	private static final IFilter<Method> defaultMethodFilter = new IFilter<Method>() {
		public boolean accept(Method m) {
			return ! (m.getDeclaringClass() == Object.class); // filter out
																												// Object.getClass()
		};
	};

	private final HashMap<Class<?>, List<IDataConnector>> connectorCache = new HashMap<Class<?>, List<IDataConnector>>();
	private final HashMap<Class<?>, List<AbstractExecutable>> executableCache = new HashMap<Class<?>, List<AbstractExecutable>>();
	private final IDataConnectorFactory connectorFactory;

	public DefaultModelInspector(IDataConnectorFactory connectorFactory) {
		this.connectorFactory = ObjectTK.enforceNotNull(connectorFactory, "connectorFactory");
	}

	@Override
	public List<AbstractExecutable> getExecutables(Class<?> cls) {
		if (executableCache.containsKey(cls)) {
			return executableCache.get(cls);

		} else {
			final List<AbstractExecutable> executables = createExecutables(cls);
			executableCache.put(cls, executables);
			return executables;
		}
	}

	@Override
	public List<IDataConnector> getConnectors(Class<?> cls) {
		if (connectorCache.containsKey(cls)) {
			return connectorCache.get(cls);

		} else {			
			final List<PendingConnectorInfo> infoQueue = new ArrayList<PendingConnectorInfo>();
			final List<IDataConnector> connectors = createConnectors(cls, infoQueue);
			connectorCache.put(cls, connectors);
			
			// We populate the connectorInfo in a second phase to avoid infinite recursions
			for (PendingConnectorInfo pendInfo : infoQueue) {
				ExposeProperties exposeAnnotation = null;
				if (pendInfo.isFieldConnector()) {
					exposeAnnotation = pendInfo.getField().getAnnotation(ExposeProperties.class);
				} else {
					exposeAnnotation = pendInfo.getGetter().getAnnotation(ExposeProperties.class);
				}
				if (exposeAnnotation != null) {
					
					// build map for connector lookup by-name
					final Class<?> valueType = pendInfo.getConnector().getType();
					final Map<String, IDataConnector> connectorMap = new HashMap<String, IDataConnector>();
					for (IDataConnector connector : getConnectors(valueType)) {
						connectorMap.put(connector.getName(), connector);
					}
					
					final List<IScalarDataConnector> nestedProperties = new ArrayList<IScalarDataConnector>();
					final List<String> unknownProperties = new ArrayList<String>();
					final List<String> illegalProperties = new ArrayList<String>();
					for (String pName : exposeAnnotation.value()) {
						final IDataConnector connector = connectorMap.get(pName);
						if (connector == null) {
							unknownProperties.add(pName);
						} else if ( ! (connector instanceof IScalarDataConnector)) {
							illegalProperties.add(pName);
						} else {
							final IScalarDataConnector scalarConnector = (IScalarDataConnector) connector;
							nestedProperties.add(scalarConnector);
						}
					}
					
					if ( ! unknownProperties.isEmpty()) {
						throw new UnknownPropertyException(valueType, unknownProperties.toArray(new String[unknownProperties.size()]));						
					} else if ( ! illegalProperties.isEmpty()) {
						throw new IllegalConnectorTypeException(IScalarDataConnector.class, valueType, illegalProperties.toArray(new String[unknownProperties.size()]));
					} else if ( ! nestedProperties.isEmpty()) {
						final IScalarDataConnector[] nestedProps = nestedProperties.toArray(new IScalarDataConnector[nestedProperties.size()]);
						final ConnectorInfo info = pendInfo.getConnector().getConnectorInfo(); 
						info.setNestedProperties(nestedProps);
					}
				}
			}
			
			return connectors;
		}
	}

	protected List<AbstractExecutable> createExecutables(Class<?> cls) {
		final List<AbstractExecutable> result = new ArrayList<AbstractExecutable>();
		
		// constructors
		final Constructor<?>[] constructors = cls.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			final Executable executableTag = constructor.getAnnotation(Executable.class);
			if (executableTag != null) {
				result.add(new ExecutableConstructor(constructor, executableTag.paramNames()));
			}
		}
		
	  // methods
		final List<Method> allMethods = ReflectionTK.getAllMethods(cls);
		for (Method method : allMethods) {
			final Executable executableTag = method.getAnnotation(Executable.class);
			if (executableTag != null) {
				result.add(new ExecutableMethod(method, executableTag.paramNames()));
			}
		}		
		
		return result;
	}
	
	protected List<IDataConnector> createConnectors(final Class<?> cls, final List<PendingConnectorInfo> infoQueue) {
		final List<IDataConnector> result = new ArrayList<IDataConnector>();
		final ExposeProperties exposeProps = cls.getAnnotation(ExposeProperties.class);
		final Level level = exposeProps != null ? exposeProps.level() : DEFAULT_LEVEL;

		// explicit specification of properties and their order via annotations
		if (exposeProps != null && exposeProps.value() != null && exposeProps.value().length > 0) {
			for (String name : exposeProps.value()) {
				final IDataConnector con = connectorFactory.createConnector(cls, name, infoQueue);
				ObjectTK.enforceNotNull(con, "con");
				result.add(con);
			}

		} else {

			// method plugs
			final List<Method> allMethods = ReflectionTK.getAllMethods(cls);
			final List<IDataConnector> methodConnectors = new ArrayList<IDataConnector>();
			final HashSet<String> propertyNames = new HashSet<String>();
			for (Method m : allMethods) {
				if ( ! defaultMethodFilter.accept(m)) continue;
				final String pName = getterToPropertyName(m, level);
				if (pName != null && ! propertyNames.contains(pName)) {
					final IDataConnector con = connectorFactory.createMethodConnector(cls, pName, infoQueue);
					ObjectTK.enforceNotNull(con, "con");
					methodConnectors.add(con);
					propertyNames.add(pName);
				}
			}

			// field plugs
			final List<Field> allFields = ReflectionTK.getAllFields(cls);
			final List<IDataConnector> fieldConnectors = new ArrayList<IDataConnector>();
			for (Field f : allFields) {
				final String pName = fieldToPropertyName(f, level);
				if (pName != null && ! propertyNames.contains(pName)) {
					f.setAccessible(true);
					final IDataConnector con = connectorFactory.createFieldConnector(cls, pName, infoQueue);
					ObjectTK.enforceNotNull(con, "con");
					fieldConnectors.add(con);
					propertyNames.add(pName);
				}
			}

			result.addAll(fieldConnectors);
			result.addAll(methodConnectors);
		}

		return Collections.unmodifiableList(result);
	}

	/**
	 * Extracts the property name from the given method
	 * 
	 * @return null if the method doesn't belong to a property
	 */
	protected String getterToPropertyName(Method m, Level level) {
		final Class<?> returnType = m.getReturnType();
		final Class<?>[] paramTypes = m.getParameterTypes();
		if (returnType != null && paramTypes.length == 0 && isExposed(m, level)) {
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

	/**
	 * Extracts the property name from the given field
	 * 
	 * @return null if the field doesn't represent a property
	 */
	protected String fieldToPropertyName(Field f, Level level) {
		return isExposed(f, level) ? f.getName() : null;
	}

	// explicitly hidden : @Hide annotation
	// explicitly exposed: @Expose annotation or public modifier
	protected boolean isExposed(Member member, Level level) {
		final AccessibleObject ao = member instanceof AccessibleObject ? (AccessibleObject) member : null;
		final Hide hide = ao != null ? ao.getAnnotation(Hide.class) : null;
		if (hide != null) return false;

		final Expose expose = ao != null ? ao.getAnnotation(Expose.class) : null;
		if (expose != null || Modifier.isPublic(member.getModifiers())) {
			return true;
		}

		if (member instanceof Field) {
			return level == Level.XRAY;
		}

		return false;
	}

	protected boolean scalar(Field f) {
		final Class<?> type = f.getType();
		return ! (Collection.class.isAssignableFrom(type)) && ! (type.isArray());
	}

}
