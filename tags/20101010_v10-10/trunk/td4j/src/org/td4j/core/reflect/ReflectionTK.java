/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2010 Michael Rauch

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

import java.beans.Introspector;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.td4j.core.tk.IFilter;


public class ReflectionTK {

	public static String decapitalize(String s) {
		return Introspector.decapitalize(s);
	}

	public static String capitalize(String s) {
		if (s == null || s.length() < 1) return null;

		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	// PEND: handle abbreviations correctly (eg. URL, nooGNU, bigZ, etc)
	public static String humanize(String s) {
		if (s == null || s.length() < 1) return null;

		final StringBuilder result = new StringBuilder(capitalize(s));
		for (int i = 1, n = result.length(); i < n; i++) {
			final char ch = result.charAt(i);
			if (Character.isUpperCase(ch) || Character.isDigit(ch)) {
				result.insert(i++, " ");
			}
		}
		return result.toString();
	}

	public static <A> A[] composeArray(A[] base, A postfix) {

		// PEND: remove ... unless necessary
		// if (postfix == null) return base;

		@SuppressWarnings("unchecked")
		final A[] result = (A[]) Array.newInstance(base.getClass().getComponentType(), base.length + 1);

		System.arraycopy(base, 0, result, 0, base.length);
		result[base.length] = postfix;
		return result;
	}

	// PEND: testcase
	public static List<Method> getMethods(Class<?> cls, IFilter<Method> filter) {
		final List<Method> result = new ArrayList<Method>();
		for (Method m : getAllMethods(cls)) {
			if (filter.accept(m)) result.add(m);
		}
		return result;
	}

	// PEND: testcase
	public static List<Method> getAllMethods(Class<?> cls) {
		final List<Method> allMethods = new ArrayList<Method>();
		allMethods.addAll(Arrays.asList(cls.getMethods()));
		for (Method m : cls.getDeclaredMethods()) {
			if ( ! allMethods.contains(m)) allMethods.add(m);
		}
		return allMethods;
	}

	// PEND: testcase
	public static List<Field> getAllFields(Class<?> cls) {
		final List<Field> allFields = new ArrayList<Field>();
		allFields.addAll(Arrays.asList(cls.getFields()));
		for (Field f : cls.getDeclaredFields()) {
			if ( ! allFields.contains(f)) allFields.add(f);
		}
		return allFields;
	}

	public static Class<?> getItemType(AccessibleObject ao) {
		
		// handle arrays
		Class<?> plainType = null;
		if (ao instanceof Field) {
			plainType = ((Field) ao).getType();
		} else if (ao instanceof Method) {
			plainType = ((Method) ao).getReturnType();
		}		
		if (plainType.isArray()) {
			final Class<?> type = plainType.getComponentType();
			return type;
		}
		
		// handle non-arrays
		Type genType = null;
		if (ao instanceof Field) {
			genType = ((Field) ao).getGenericType();
		} else if (ao instanceof Method) {
			genType = ((Method) ao).getGenericReturnType();
		}

		// get parameterized type
		if (genType instanceof ParameterizedType) {
			final ParameterizedType type = (ParameterizedType) genType;
			final Type[] typeArgs = type.getActualTypeArguments();
			if (typeArgs.length == 1) {
				if (typeArgs[0] instanceof Class) {
					return (Class<?>) typeArgs[0];
				} else if (typeArgs[0] instanceof ParameterizedType) {
					final ParameterizedType paramTypeArg = (ParameterizedType) typeArgs[0];
					final Type rawType = paramTypeArg.getRawType();
					if (rawType instanceof Class) {
						return (Class<?>) rawType;
					}
				} else if (typeArgs[0] instanceof WildcardType) {
					final WildcardType wildcardTypeArg = (WildcardType) typeArgs[0];
					final Type[] upperBoundsTypes = wildcardTypeArg.getUpperBounds();
					if (upperBoundsTypes.length == 1 && upperBoundsTypes[0] instanceof Class) {
						return (Class<?>) upperBoundsTypes[0];
					}
				}
			}
			
			// Without generic type declaration, genType = type
		} else if (genType instanceof Class) {
			final Class<?> type = (Class<?>) genType;
			if (Collection.class.isAssignableFrom(type)) {
				return Object.class;
			} else {
				return type;
			}
		}

		return null;
	}

}
