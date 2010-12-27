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
import java.util.Collection;


public class ListFieldConnector extends AbstractListFieldConnector {

	public ListFieldConnector(Class<?> contextType, Field field, Class<?> valueType) {
		super(contextType, field, valueType);
		
		if ( ! Collection.class.isAssignableFrom(field.getType())) throw new IllegalArgumentException("not a collection type: " + field.getType());
	}
	
	@Override
	protected Collection<?> readValue0(Object ctx) throws Exception {
		return (Collection<?>) getField().get(ctx);
	}
	
	@Override
	public String toString() {
		return getContextType().getName() + "#" + getField().getName() + " : List<" + getValueType() + ">";
	}

}
