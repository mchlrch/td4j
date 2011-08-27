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


import ch.miranet.commons.TK;


public class UnknownPropertyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private static String prepareSinglePropertyMsg(Class<?> cls, String propertyName) {
		return String.format("Property not found: %1$s#%2$s", cls.getName(), TK.Strings.assertNotEmpty(propertyName, "propertyName"));
	}
	
	private static String prepareMultiPropertiesMsg(Class<?> cls, String... propertyNames) {
		TK.Arrays.assertNotEmpty(propertyNames, "propertyNames");
		final StringBuilder sb = new StringBuilder("Properties not found " + cls.getName() + ": ");
		boolean firstElement = true;
		for (String pName : propertyNames) {
			if ( ! firstElement) sb.append(", ");
			firstElement = false;

			sb.append(String.format("#%1$s", pName));
		}
		
		return sb.toString();
	}
	
	public UnknownPropertyException(Class<?> cls, String... propertyNames) {
		super(propertyNames.length > 1 ? prepareMultiPropertiesMsg(cls, propertyNames) : prepareSinglePropertyMsg(cls, propertyNames[0]));
	}
	
}