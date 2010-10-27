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


import ch.miranet.commons.ArrayTK;
import ch.miranet.commons.StringTK;


public class IllegalConnectorTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private static String prepareSinglePropertyMsg(Class<?> expectedType, Class<?> cls, String propertyName) {
		StringTK.enforceNotEmpty(propertyName, "propertyName");
		return String.format("Illegal connector type, %1$s expected: %2$s#%3$s", expectedType.getName(), cls.getName(), propertyName);
	}
	
	private static String prepareMultiPropertiesMsg(Class<?> expectedType, Class<?> cls, String... propertyNames) {
		ArrayTK.enforceNotEmpty(propertyNames, "propertyNames");
		final StringBuilder sb = new StringBuilder("Illegal connector type, ");
		sb.append(expectedType.getName()).append(" expected: ");
		boolean firstElement = true;
		for (String pName : propertyNames) {
			if ( ! firstElement) {
				sb.append(", ");
				firstElement = false;
			}
			sb.append(String.format("%1$s#%2$s", cls.getName(), pName));
		}
		
		return sb.toString();
	}
	
	public IllegalConnectorTypeException(Class<?> expectedType, Class<?> cls, String... propertyNames) {
		super(propertyNames.length > 1 ? prepareMultiPropertiesMsg(expectedType, cls, propertyNames) : prepareSinglePropertyMsg(expectedType, cls, propertyNames[0]));
	}
	
}