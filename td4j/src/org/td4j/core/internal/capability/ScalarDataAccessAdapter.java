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

import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.tk.ObjectTK;

// TODO: remove as soon as dataConnectors implement XYDataAccess
public class ScalarDataAccessAdapter implements ScalarDataAccess {

	private final IScalarDataConnector connector;
	
	public ScalarDataAccessAdapter(IScalarDataConnector connector) {
		this.connector = ObjectTK.enforceNotNull(connector, "connector");
	}
	
	public IScalarDataConnector getConnector() {
		return connector;
	}
	
	@Override
	public boolean canRead(Object ctx) {
		return connector.canRead(ctx);
	}

	@Override
	public boolean canWrite(Object ctx) {
		return connector.canWrite(ctx);
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
	public Object readValue(Object ctx) {
		return connector.readValue(ctx);
	}

	@Override
	public void writeValue(Object ctx, Object val) {
		connector.writeValue(ctx, val);
	}

}
