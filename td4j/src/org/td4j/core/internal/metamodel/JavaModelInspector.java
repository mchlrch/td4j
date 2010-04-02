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

package org.td4j.core.internal.metamodel;

import java.lang.reflect.AccessibleObject;
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

import org.td4j.core.binding.model.CollectionDataConnector;
import org.td4j.core.binding.model.DataConnector;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.ScalarDataConnector;
import org.td4j.core.internal.binding.model.CollectionFieldConnector;
import org.td4j.core.internal.binding.model.CollectionMethodConnector;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.container.Operations;
import org.td4j.core.reflect.Expose;
import org.td4j.core.reflect.ExposeProperties;
import org.td4j.core.reflect.Hide;
import org.td4j.core.reflect.IllegalConnectorTypeException;
import org.td4j.core.reflect.ListProperty;
import org.td4j.core.reflect.NamedDataConnector;
import org.td4j.core.reflect.ReflectionTK;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.reflect.UnknownPropertyException;
import org.td4j.core.tk.IFilter;
import org.td4j.core.tk.ObjectTK;

public class JavaModelInspector {
	
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
	
	private final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();
	private final JavaMetaModel metaModel;
	
	JavaModelInspector(JavaMetaModel metaModel) {
		this.metaModel = ObjectTK.enforceNotNull(metaModel, "metaModel");
	}
	
	
	public FeatureContainer createFeatures(Class<?> cls) {
		final FeatureContainer features = new FeatureContainer();
		
		final List<NamedDataConnector> connectors = createConnectors(cls);		
		for (NamedDataConnector nc : connectors) {
			final DataConnector connector = nc.getConnector();
			
			if (connector instanceof ScalarDataConnector) {
				final ScalarDataConnector scalarConnector = (ScalarDataConnector) connector;
				final ScalarProperty scalarProperty = new ScalarProperty(nc.getName(), scalarConnector);
				features.scalarProperties.add(scalarProperty);
				
			} else if (connector instanceof CollectionDataConnector) {
				final CollectionDataConnector collectionConnector = (CollectionDataConnector) connector;
				final ScalarProperty[] nestedProperties = findNestedProperties(connector);
				final ListProperty listProperty = new ListProperty(nc.getName(), collectionConnector, nestedProperties);
				features.listProperties.add(listProperty);
				
			} else {
				throw new IllegalStateException("Unsupported connector type: " + connector.getClass());
			}
		}
		
		return features;
	}
	
	private void createOperations(JavaMetaClass<?> metaClass) {
		Operations operations;
		// TODO _0 implement
		
	}
	
	
	// =======================================================================================================
	
	private List<NamedDataConnector> createConnectors(final Class<?> cls) {
		final List<NamedDataConnector> result = new ArrayList<NamedDataConnector>();
		final ExposeProperties exposeProps = cls.getAnnotation(ExposeProperties.class);
		final Level level = exposeProps != null ? exposeProps.level() : DEFAULT_LEVEL;

		// explicit specification of properties and their order via annotations
		if (exposeProps != null && exposeProps.value() != null && exposeProps.value().length > 0) {
			for (String name : exposeProps.value()) {
				final DataConnector con = connectorFactory.createConnector(cls, name);
				ObjectTK.enforceNotNull(con, "con");
				result.add(new NamedDataConnector(con, name));
			}

		} else {

			final HashSet<String> propertyNames = new HashSet<String>();
			final List<NamedDataConnector> methodConnectors = createDefaultMethodConnectors(cls, level, propertyNames);
			final List<NamedDataConnector> fieldConnectors = createDefaultFieldConnectors(cls, level, propertyNames);

			result.addAll(fieldConnectors);
			result.addAll(methodConnectors);
		}
		
		return Collections.unmodifiableList(result);
	}
	
	
	
	private List<NamedDataConnector> createDefaultMethodConnectors(final Class<?> cls, final Level level, final HashSet<String> propertyNames) {
		final List<Method> allMethods = ReflectionTK.getAllMethods(cls);
		final List<NamedDataConnector> methodConnectors = new ArrayList<NamedDataConnector>();
		for (Method m : allMethods) {
			if ( ! defaultMethodFilter.accept(m)) continue;
			final String pName = getterToPropertyName(m, level);
			if (pName != null && ! propertyNames.contains(pName)) {
				final DataConnector con = connectorFactory.createMethodConnector(cls, pName);
				ObjectTK.enforceNotNull(con, "con");
				methodConnectors.add(new NamedDataConnector(con, pName));
				propertyNames.add(pName);
			}
		}
		return methodConnectors;
	}

	private List<NamedDataConnector> createDefaultFieldConnectors(final Class<?> cls, final Level level, final HashSet<String> propertyNames) {
		final List<Field> allFields = ReflectionTK.getAllFields(cls);
		final List<NamedDataConnector> fieldConnectors = new ArrayList<NamedDataConnector>();
		for (Field f : allFields) {
			final String pName = fieldToPropertyName(f, level);
			if (pName != null && ! propertyNames.contains(pName)) {
				f.setAccessible(true);
				final DataConnector con = connectorFactory.createFieldConnector(cls, pName);
				ObjectTK.enforceNotNull(con, "con");
				fieldConnectors.add(new NamedDataConnector(con, pName));
				propertyNames.add(pName);
			}
		}
		return fieldConnectors;
	}
	
	// =======================================================================================================
	
	// nested properties are only supported for collection connectors
	private ScalarProperty[] findNestedProperties(final DataConnector connector) {
		final Class<?> valueType = connector.getValueType();
		if (connector instanceof CollectionFieldConnector) {
			final CollectionFieldConnector cfConnector = (CollectionFieldConnector) connector;
			final ExposeProperties exposeAnnotation = cfConnector.getField().getAnnotation(ExposeProperties.class);
			return findNestedProperties(valueType, exposeAnnotation);
			
		} else if (connector instanceof CollectionMethodConnector) {
			final CollectionMethodConnector cmConnector = (CollectionMethodConnector) connector;
			final ExposeProperties exposeAnnotation = cmConnector.getGetterMethod().getAnnotation(ExposeProperties.class);
			return findNestedProperties(valueType, exposeAnnotation);
			
		} else {
			return new ScalarProperty[0];
		}
	}
	
	private ScalarProperty[] findNestedProperties(final Class<?> valueType, final ExposeProperties exposeAnnotation) {
		if (exposeAnnotation == null) return new ScalarProperty[0];
		
		// TODO: simplify this once properties can be retrieved by-name from MetaClass
		
		// build map for property lookup by-name
		final MetaClass mc = metaModel.getMetaClass(valueType);
		final Map<String, ScalarProperty> scalarPropertyMap = new HashMap<String, ScalarProperty>();
		final Map<String, ListProperty> listPropertyMap = new HashMap<String, ListProperty>();
		
		for (ScalarProperty prop : mc.getScalarProperties()) {
			scalarPropertyMap.put(prop.getName(), prop);
		}
		for (ListProperty prop : mc.getListProperties()) {
			listPropertyMap.put(prop.getName(), prop);
		}		
		
		final List<ScalarProperty> nestedProperties = new ArrayList<ScalarProperty>();
		final List<String> unknownProperties = new ArrayList<String>();
		final List<String> illegalProperties = new ArrayList<String>();
		for (String pName : exposeAnnotation.value()) {
			final ScalarProperty prop = scalarPropertyMap.get(pName);
			if (prop != null) {
				nestedProperties.add(prop);
			} else if (listPropertyMap.containsKey(pName)) {
				illegalProperties.add(pName);				
			} else {
				unknownProperties.add(pName);				
			}
		}
		
		if ( ! unknownProperties.isEmpty()) {
			throw new UnknownPropertyException(valueType, unknownProperties.toArray(new String[unknownProperties.size()]));
			
		} else if ( ! illegalProperties.isEmpty()) {
			throw new IllegalConnectorTypeException(ScalarDataConnector.class, valueType, illegalProperties.toArray(new String[unknownProperties.size()]));
			
		}
		
		return nestedProperties.toArray(new ScalarProperty[nestedProperties.size()]);
	}
	
	// =======================================================================================================
	
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
	private String fieldToPropertyName(Field f, Level level) {
		return isExposed(f, level) ? f.getName() : null;
	}
	
	// explicitly hidden : @Hide annotation
	// explicitly exposed: @Expose annotation or public modifier
	private boolean isExposed(Member member, Level level) {
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

	private boolean scalar(Field f) {
		final Class<?> type = f.getType();
		return ! (Collection.class.isAssignableFrom(type)) && ! (type.isArray());
	}
	
	
	// =======================================================================================================
	public static class FeatureContainer {
		public final List<ScalarProperty> scalarProperties = new ArrayList<ScalarProperty>();
		public final List<ListProperty> listProperties = new ArrayList<ListProperty>();
	}


}
