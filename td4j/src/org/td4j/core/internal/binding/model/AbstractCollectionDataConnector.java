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

import java.util.Collection;

import org.td4j.core.binding.model.CollectionDataProxy;
import org.td4j.core.binding.model.ICollectionDataConnector;



public abstract class AbstractCollectionDataConnector extends AbstractDataConnector implements ICollectionDataConnector {

	private final Class<?> collectionType;

	protected AbstractCollectionDataConnector(Class<?> modelType, Class<?> collectionType, Class<?> valueType) {
		super(modelType, valueType);
		if (collectionType == null) throw new NullPointerException("collectionType");

		this.collectionType = collectionType;
	}

	public Class<?> getCollectionType() {
		return collectionType;
	}

	public Collection<?> readValue(Object model) {
		if (model == null || ! canRead(model)) return null;

		try {
			return readValue0(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract Collection<?> readValue0(Object model) throws Exception;

	protected abstract String getPropertyName();
	
	
	public CollectionDataProxy createProxy() {
		return new CollectionDataProxy(this, getPropertyName());
	}

}
