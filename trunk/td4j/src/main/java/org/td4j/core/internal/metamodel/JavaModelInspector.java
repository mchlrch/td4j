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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.ListDataConnector;
import org.td4j.core.internal.binding.model.AbstractListFieldConnector;
import org.td4j.core.internal.binding.model.AbstractListMethodConnector;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.internal.reflect.AbstractOperation;
import org.td4j.core.internal.reflect.CompanionMethodOperation;
import org.td4j.core.internal.reflect.ConstructorOperation;
import org.td4j.core.internal.reflect.MethodOperation;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.MetaClassProvider;
import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.DataConnector;
import org.td4j.core.reflect.Hide;
import org.td4j.core.reflect.IllegalConnectorTypeException;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.reflect.Operation;
import org.td4j.core.reflect.Show;
import org.td4j.core.reflect.ShowProperties;
import org.td4j.core.reflect.UnknownPropertyException;

import ch.miranet.commons.TK;
import ch.miranet.commons.filter.Filter;
import ch.miranet.commons.service.SvcProvider;

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
	
	private static final Filter<Method> defaultMethodFilter = new Filter<Method>() {
		public boolean accept(Method m) {
			return ! (m.getDeclaringClass() == Object.class); // filter out Object.getClass()
		}
	};
	
	private final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();
	
	
	public FeatureContainer createShallowFeatures(final Class<?> cls, final SvcProvider svcProvider, final MetaClassProvider metaClassProvider) {
		final FeatureContainer features = new FeatureContainer();

		populateConnectors(cls, features, metaClassProvider);
		
		features.operations.addAll(createOperations(cls, svcProvider));
		
		return features;
	}
	
	private void populateConnectors(final Class<?> cls, final FeatureContainer features, final MetaClassProvider metaClassProvider) {
		final List<NamedDataConnector> connectors = createConnectors(cls);
		final Map<String, NamedDataConnector> connMap = asMap(connectors);
		
		// First round only marks choiceConnectors to avoid them from beeing treated as 'normal' listProperties
		// marking is done by linking the pair of NamedDataConnectors together
		for (NamedDataConnector nc : connectors) {
			final DataConnector connector = nc.connector;
			if (connector instanceof IndividualDataConnector) {
				
				// try to find choiceOptions
				ListDataConnector choiceOptionsConnector = null;
				final NamedDataConnector optConn = connMap.get(choiceOptionsNameFor(nc.name));
				if (optConn != null && optConn.connector instanceof ListDataConnector) {					
					choiceOptionsConnector = (ListDataConnector) optConn.connector;
					if (connector.getValueType().isAssignableFrom(choiceOptionsConnector.getValueType())) {
						optConn.setChoiceConsumer( (IndividualDataConnector)connector );
						nc.setChoiceProvider(choiceOptionsConnector);
					}				
				}
			}
		}
		
		
		for (NamedDataConnector nc : connectors) {
			final DataConnector connector = nc.connector;
			
			// trigger creation of referenced metaClasses
			final Class<?> valueType = connector.getValueType();
			metaClassProvider.getMetaClass(valueType);
			
			if (connector instanceof IndividualDataConnector) {
				final IndividualDataConnector individualConnector = (IndividualDataConnector) connector;				
				final ListDataConnector choiceOptionsConnector = nc.getChoiceProvider();				
				final IndividualProperty individualProperty = new IndividualProperty(nc.name, individualConnector, choiceOptionsConnector);
				features.individualProperties.add(individualProperty);
				
			} else if (connector instanceof ListDataConnector) {
				
				// ignore choiceOption providers
				if (nc.isChoiceProvider()) continue;
				
				final ListDataConnector listConnector = (ListDataConnector) connector;
				final MutableListProperty listProperty = new MutableListProperty(nc.name, listConnector);
				features.listProperties.add(listProperty);
				
			} else {
				throw new IllegalStateException("Unsupported connector type: " + connector.getClass());
			}
		}		
	}
	
	private Map<String, NamedDataConnector> asMap(List<NamedDataConnector> connectors) {
		final Map<String, NamedDataConnector> result = new HashMap<String, NamedDataConnector>();
		for (NamedDataConnector nc : connectors) {
			final String name = nc.name;
			if (result.containsKey(name)) throw new IllegalStateException("duplicate connector name: " + name);
			result.put(name, nc);			
		}
		return result;
	}
	
	public void refineShallowFeatures(final FeatureContainer features, final SvcProvider svcProvider, final MetaClassProvider metaClassProvider) {
		for (MutableListProperty prop : features.listProperties) {
			final ListDataConnector listConnector = prop.getDataConnector();
			final IndividualProperty[] nestedProperties = findNestedProperties(listConnector, metaClassProvider);
			prop.setNestedProperties(nestedProperties);
		}
	}	
	
	
	// =======================================================================================================
	
	private List<NamedDataConnector> createConnectors(final Class<?> cls) {
		final List<NamedDataConnector> result = new ArrayList<NamedDataConnector>();
		final ShowProperties exposeProps = cls.getAnnotation(ShowProperties.class);
		final Level level = exposeProps != null ? exposeProps.level() : DEFAULT_LEVEL;

		// explicit specification of properties and their order via annotations
		if (exposeProps != null && exposeProps.value() != null && exposeProps.value().length > 0) {
			for (String name : exposeProps.value()) {
				final DataConnector con = connectorFactory.createConnector(cls, name);
				TK.Objects.assertNotNull(con, "con");
				final NamedDataConnector nc = new NamedDataConnector(con, name);
				result.add(nc);
				
				// try to get connector for choiceOptions
				final NamedDataConnector choiceOptionsConnector = createOptionsConnector(cls, nc);
				if (choiceOptionsConnector != null) {
					result.add(choiceOptionsConnector);
				}				
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
	
	private NamedDataConnector createOptionsConnector(final Class<?> cls, NamedDataConnector nc) {
		final String optionsName = choiceOptionsNameFor(nc.name);
		try {
			final DataConnector con = connectorFactory.createConnector(cls, optionsName);			
			if (con instanceof ListDataConnector) {
				return new NamedDataConnector(con, optionsName);
			}
		} catch (UnknownPropertyException ex) {
			// no options for this property - ignore
		}
		return null;
	}	
	
	private String choiceOptionsNameFor(String baseName) {
		return baseName + "Options";
	}
	
	private List<NamedDataConnector> createDefaultMethodConnectors(final Class<?> cls, final Level level, final HashSet<String> propertyNames) {
		final List<Method> allMethods = TK.Reflection.getAllMethods(cls);
		final List<NamedDataConnector> methodConnectors = new ArrayList<NamedDataConnector>();
		for (Method m : allMethods) {
			if ( ! defaultMethodFilter.accept(m)) continue;
			final String pName = getterToPropertyName(m, level);
			if (pName != null && ! propertyNames.contains(pName)) {
				final DataConnector con = connectorFactory.createMethodConnector(cls, pName);
				TK.Objects.assertNotNull(con, "con");
				methodConnectors.add(new NamedDataConnector(con, pName));
				propertyNames.add(pName);
			}
		}
		return methodConnectors;
	}

	private List<NamedDataConnector> createDefaultFieldConnectors(final Class<?> cls, final Level level, final HashSet<String> propertyNames) {
		final List<Field> allFields = TK.Reflection.getAllFields(cls);
		final List<NamedDataConnector> fieldConnectors = new ArrayList<NamedDataConnector>();
		for (Field f : allFields) {
			final String pName = fieldToPropertyName(f, level);
			if (pName != null && ! propertyNames.contains(pName)) {
				f.setAccessible(true);
				final DataConnector con = connectorFactory.createFieldConnector(cls, pName);
				TK.Objects.assertNotNull(con, "con");
				fieldConnectors.add(new NamedDataConnector(con, pName));
				propertyNames.add(pName);
			}
		}
		return fieldConnectors;
	}
	
	// PEND: use OperationGroups to bundle operations together
	private List<AbstractOperation> createOperations(final Class<?> cls, SvcProvider svcProvider) {
		final List<AbstractOperation> result = new ArrayList<AbstractOperation>();

		// constructors
		final Constructor<?>[] constructors = cls.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			final Operation operationTag = constructor.getAnnotation(Operation.class);
			if (operationTag != null) {
				result.add(new ConstructorOperation(constructor, operationTag.paramNames()));
			}
		}

		// methods
		final List<Method> allMethods = TK.Reflection.getAllMethods(cls);
		for (Method method : allMethods) {
			final Operation operationTag = method.getAnnotation(Operation.class);
			if (operationTag != null) {
				result.add(new MethodOperation(method, operationTag.paramNames()));
			}
		}

		// companions
		final Companions companionSpec = cls.getAnnotation(Companions.class);
		if (companionSpec != null) {
			for (Class<?> compCls : companionSpec.value()) {
				final Object svc = svcProvider.requireService(compCls);
	
				final List<Method> compMethods = TK.Reflection.getAllMethods(compCls);
				for (Method method : compMethods) {
					final Operation operationTag = method.getAnnotation(Operation.class);
					if (operationTag != null) {
						result.add(new CompanionMethodOperation(cls, svc, method, operationTag.paramNames()));
					}
				}
			}
		}

		return result;
	}
	
	// =======================================================================================================
	
	// nested properties are only supported for list connectors
	private IndividualProperty[] findNestedProperties(final DataConnector connector, final MetaClassProvider metaClassProvider) {
		final Class<?> valueType = connector.getValueType();
		if (connector instanceof AbstractListFieldConnector) {
			final AbstractListFieldConnector cfConnector = (AbstractListFieldConnector) connector;
			final ShowProperties exposeAnnotation = cfConnector.getField().getAnnotation(ShowProperties.class);
			return findNestedProperties(valueType, exposeAnnotation, metaClassProvider);
			
		} else if (connector instanceof AbstractListMethodConnector) {
			final AbstractListMethodConnector cmConnector = (AbstractListMethodConnector) connector;
			final ShowProperties exposeAnnotation = cmConnector.getGetterMethod().getAnnotation(ShowProperties.class);
			return findNestedProperties(valueType, exposeAnnotation, metaClassProvider);
			
		} else {
			return new IndividualProperty[0];
		}
	}
	
	private IndividualProperty[] findNestedProperties(final Class<?> valueType, final ShowProperties exposeAnnotation, final MetaClassProvider metaClassProvider) {
		if (exposeAnnotation == null) return new IndividualProperty[0];
		
		final MetaClass mc = metaClassProvider.getMetaClass(valueType);
		
		final List<IndividualProperty> nestedProperties = new ArrayList<IndividualProperty>();
		final List<String> unknownProperties = new ArrayList<String>();
		final List<String> illegalProperties = new ArrayList<String>();

		for (String pName : exposeAnnotation.value()) {
			final IndividualProperty prop = mc.getIndividualProperty(pName);
			if (prop != null) {
				nestedProperties.add(prop);
			} else if (mc.getListProperty(pName) != null) {
				illegalProperties.add(pName);				
			} else {
				unknownProperties.add(pName);				
			}
		}
		
		if ( ! unknownProperties.isEmpty()) {
			throw new UnknownPropertyException(valueType, unknownProperties.toArray(new String[unknownProperties.size()]));
			
		} else if ( ! illegalProperties.isEmpty()) {
			throw new IllegalConnectorTypeException(IndividualDataConnector.class, valueType, illegalProperties.toArray(new String[unknownProperties.size()]));
			
		}
		
		return nestedProperties.toArray(new IndividualProperty[nestedProperties.size()]);
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
				return TK.Strings.decapitalize(name.substring(3));

			} else if (name.startsWith("is") && name.length() > 2) {
				if (Boolean.class.isAssignableFrom(returnType) || boolean.class.isAssignableFrom(returnType)) {
					return TK.Strings.decapitalize(name.substring(2));
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

		final Show expose = ao != null ? ao.getAnnotation(Show.class) : null;
		if (expose != null || Modifier.isPublic(member.getModifiers())) {
			return true;
		}

		if (member instanceof Field) {
			return level == Level.XRAY;
		}

		return false;
	}
	
	// =======================================================================================================
	public static class FeatureContainer {
		public final List<IndividualProperty> individualProperties = new ArrayList<IndividualProperty>();
		public final List<MutableListProperty> listProperties = new ArrayList<MutableListProperty>();
		public final List<AbstractOperation> operations = new ArrayList<AbstractOperation>();
	}
	
	
	private static class NamedDataConnector {
		
		private final DataConnector connector;
		private final String name;
		
		private ListDataConnector       choiceProvider;
		private IndividualDataConnector choiceConsumer;
		
		private NamedDataConnector(DataConnector connector, String name) {
			this.connector = TK.Objects.assertNotNull(connector, "connector");
			this.name = TK.Strings.assertNotEmpty(name, "name");
		}
		
		private void setChoiceProvider(ListDataConnector conn) { this.choiceProvider = conn; }
		private ListDataConnector getChoiceProvider()          { return choiceProvider; }
		
		private void setChoiceConsumer(IndividualDataConnector conn) { this.choiceConsumer = conn; }
		private boolean isChoiceProvider()                           { return choiceConsumer != null; }
				
		@Override
		public String toString() {
			return name;
		}
	}
	

}
