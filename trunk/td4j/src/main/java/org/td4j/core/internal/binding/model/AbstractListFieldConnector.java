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
import java.lang.reflect.Modifier;

import ch.miranet.commons.ObjectTK;


public abstract class AbstractListFieldConnector extends AbstractListDataConnector {

	private final Field field;

	public AbstractListFieldConnector(Class<?> contextType, Field field, Class<?> valueType) {
		super(contextType, valueType);

		this.field = ObjectTK.enforceNotNull(field, "field");		
	}
	
	public Field getField() {
		return field;
	}

	public boolean canRead()  { return true; }
	
	public boolean canRead(Object ctx) {
		return canRead() && (ctx != null || Modifier.isStatic(field.getModifiers()));
	}
	
	@Override
	public int hashCode() {
		return 41 * super.hashCode() + field.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractListFieldConnector) {
			final AbstractListFieldConnector that = (AbstractListFieldConnector) other;
			return super.equals(other) 
					&& that.canEqual(this)
					&& this.field.equals(that.field);
		} else {
			return false;
		}
	}

}
