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

import ch.miranet.commons.TK;

public class FieldConnectorParts {
	
	private final Field field;
	
	
	public FieldConnectorParts(Field field) {
		this.field = field;
		
		if ( ! field.isAccessible())  field.setAccessible(true);
	}
	
	public Field getField() { return field;	}
	
	public boolean canRead()  { return true; }
	public boolean canWrite() { return ! Modifier.isFinal(field.getModifiers()); }

	public boolean isFieldStatic() { return Modifier.isStatic(field.getModifiers()); }
	
	
	public Object readFromField(Object ctx) throws Exception {
		TK.Objects.assertNotNull(ctx, "ctx");
		return field.get(ctx);
	}

	public void writeToField(Object ctx, Object val) throws Exception {
		TK.Objects.assertNotNull(ctx, "ctx");
		field.set(ctx, val);
	}
	
	public String toString() {
		return field.getName();
	}
	
	public int hashCode() {
		return field.hashCode();
	}
	
	public boolean equals(Object other) {
		if (other instanceof FieldConnectorParts) {
			final FieldConnectorParts that = (FieldConnectorParts) other;
			return that.canEqual(this)
					&& this.field.equals(that.field);
		} else {
			return false;
		}
	}
	
	public boolean canEqual(Object other) {
		return other instanceof FieldConnectorParts;
	}	
	
}
