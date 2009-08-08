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

import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;

public class PropertySignature {
	private final Class<?> type;
	private final String propertyName;
	private final Class<?>[] parameterTypes;
	
	private final Method getter;
	private final Method setter;	

	private volatile int hashCode = 0;

	public PropertySignature(String propertyName, Class<?> type, Class<?>[] parameterTypes, Method getter, Method setter) {
		this.type = ObjectTK.enforceNotNull(type, "type");
		this.propertyName = StringTK.enforceNotEmpty(propertyName, "propertyName");
		this.parameterTypes = ObjectTK.enforceNotNull(parameterTypes, "parameterTypes");
		
		this.getter = ObjectTK.enforceNotNull(getter, "getter");
		this.setter = setter;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	
	public Method getGetter() {
		return getter;
	}
	
	public Method getSetter() {
		return setter;
	}
	
	public boolean isWritable() {
		return setter != null;
	}
	

	public boolean equals(Object o2) {
		if (this == o2) {
			return true;
		}
		PropertySignature that = (PropertySignature) o2;
		if ( ! (type == that.type)) {
			return false;
		}
		if ( ! (propertyName.equals(that.propertyName))) {
			return false;
		}
		if (parameterTypes.length != that.parameterTypes.length) {
			return false;
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			if ( ! (parameterTypes[i] == that.parameterTypes[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Hash code computed using algorithm suggested in Effective Java, Item 8.
	 */
	public int hashCode() {
		if (hashCode == 0) {
			int result = 17;
			result = 37 * result + type.hashCode();
			result = 37 * result + propertyName.hashCode();
			if (parameterTypes != null) {
				for (int i = 0; i < parameterTypes.length; i++) {
					result = 37 * result + ((parameterTypes[i] == null) ? 0 : parameterTypes[i].hashCode());
				}
			}
			hashCode = result;
		}
		return hashCode;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		for (Class<?> paramType : parameterTypes) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(paramType.getSimpleName());
		}
		sb.append(")");
		sb.insert(0, " (");
		
		sb.insert(0, (isWritable() ? "W] " : "]  "));
		sb.insert(0, " [R");
		
		sb.insert(0, type.getSimpleName());
		sb.insert(0, ": ").insert(0, propertyName);
		
		return sb.toString();
	}
}