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

package org.td4j.core.internal.binding.model;

import java.lang.reflect.Field;
import java.util.Collection;


public class CollectionFieldConnector extends AbstractCollectionDataConnector {

	private final Field field;

	public CollectionFieldConnector(Class<?> modelType, Field field, Class<?> valueType) {
		super(modelType, field.getType(), valueType);

		if ( ! Collection.class.isAssignableFrom(field.getType())) throw new IllegalArgumentException("not a collection type: " + field.getType());
		this.field = field;
	}

	public boolean canRead(Object model) {
		return model != null;
	}

	@Override
	protected Collection<?> readValue0(Object model) throws Exception {
		return (Collection<?>) field.get(model);
	}

	@Override
	public String getName() {
		return field.getName();
	}
	
	@Override
	public String toString() {
		return getModelType().getName() + "#" + field.getName() + " : " + getCollectionType().getName() + "<" + getType() + ">";
	}

}
