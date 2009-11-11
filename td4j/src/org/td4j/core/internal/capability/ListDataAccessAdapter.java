/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009 Michael Rauch

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

package org.td4j.core.internal.capability;

import java.util.Collection;
import java.util.List;

import org.td4j.core.binding.model.ICollectionDataConnector;
import org.td4j.core.tk.ObjectTK;

// TODO: this is only a temporary workaround solution
public class ListDataAccessAdapter implements ListDataAccess {

	private final ICollectionDataConnector connector;
	
	public ListDataAccessAdapter(ICollectionDataConnector connector) {
		this.connector = ObjectTK.enforceNotNull(connector, "connector");
	}
	
	@Override
	public boolean canRead(Object ctx) {
		return connector.canRead(ctx);
	}

	@Override
	public Class<?> getContextType() {
		return connector.getModelType();
	}

	@Override
	public Class<?> getValueType() {
		return connector.getType();
	}

	@Override
	public List<?> readValue(Object ctx) {
		final Collection<?> result = connector.readValue(ctx);
		return (List<?>) result;
	}
	
}
