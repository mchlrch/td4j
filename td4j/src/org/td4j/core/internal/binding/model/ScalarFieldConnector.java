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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.td4j.core.tk.ObjectTK;



public class ScalarFieldConnector extends AbstractScalarDataConnector {

	private final Field field;

	public ScalarFieldConnector(Class<?> modelType, Field field) {
		super(modelType, field.getType());

		this.field = field;
	}
	
	public Field getField() {
		return field;
	}

	public boolean canRead(Object ctx) {
		return ctx != null;
	}

	public boolean canWrite(Object ctx) {
		return ! Modifier.isFinal(field.getModifiers());
	}

	@Override
	protected Object readValue0(Object ctx) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		return field.get(ctx);
	}

	@Override
	protected void writeValue0(Object ctx, Object val) throws Exception {
		ObjectTK.enforceNotNull(ctx, "ctx");
		field.set(ctx, val);
	}

	@Override
	public String toString() {
		return getContextType().getName() + "#" + field.getName();
	}

}
