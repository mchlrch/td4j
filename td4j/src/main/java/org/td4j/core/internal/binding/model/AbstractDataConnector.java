/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

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

import org.td4j.core.reflect.DataConnector;

import ch.miranet.commons.ObjectTK;

public abstract class AbstractDataConnector implements DataConnector {

	private final Class<?> ctxType;
	private final Class<?> valueType;

	protected AbstractDataConnector(Class<?> ctxType, Class<?> valueType) {
		this.ctxType = ObjectTK.enforceNotNull(ctxType, "ctxType");
		this.valueType = ObjectTK.enforceNotNull(valueType, "valueType");
	}

	public Class<?> getContextType() {
		return ctxType;
	}

	public Class<?> getValueType() {
		return valueType;
	}
	
	@Override
	public int hashCode() {
		return 41 * (41 + ctxType.hashCode()) + valueType.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractDataConnector) {
			final AbstractDataConnector that = (AbstractDataConnector) other;
			return that.canEqual(this)
					&& this.ctxType.equals(that.ctxType)
					&& this.valueType.equals(that.valueType);
			
		} else {
			return false;
		}
	}
	
	public abstract boolean canEqual(Object other);
	
}
