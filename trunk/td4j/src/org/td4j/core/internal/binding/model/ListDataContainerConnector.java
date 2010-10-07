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

import java.util.Collection;

import org.td4j.core.binding.model.ListDataContainer;


public class ListDataContainerConnector extends AbstractListDataConnector {

	public ListDataContainerConnector(Class<?> valueType) {
		super(ListDataContainer.class, valueType);
	}

	public boolean canRead()  { return true; }
	public boolean canWrite() { return true; }
	
	public boolean canRead(Object ctx) {
		return ctx != null && contextAsContainer(ctx).canRead();
	}

	public boolean canWrite(Object ctx) {
		return ctx != null && contextAsContainer(ctx).canWrite();
	}

	@Override
	protected Collection<?> readValue0(Object ctx) throws Exception {
		return contextAsContainer(ctx).getContent();
	}

	@Override
	public String toString() {
		return getClass().getName() + ": valueType=" + getValueType();
	}

	protected ListDataContainer<Object> contextAsContainer(Object ctx) {
		@SuppressWarnings("unchecked")
		final ListDataContainer<Object> container = (ListDataContainer<Object>) ctx;

		return container;
	}

}
