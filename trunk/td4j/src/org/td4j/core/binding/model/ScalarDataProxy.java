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

package org.td4j.core.binding.model;

import org.td4j.core.internal.binding.model.DataProxy;

public class ScalarDataProxy extends DataProxy<IScalarDataConnector> {

	public ScalarDataProxy(IScalarDataConnector connector, String propertyName) {
		super(connector, propertyName);
	}

	public boolean canRead() {
		return connector.canRead(getModel());
	}

	public boolean canWrite() {
		return connector.canWrite(getModel());
	}

	// PEND: use converter
	public Object readValue() {
		return connector.readValue(getModel());
	}

	// PEND: use converter
	public void writeValue(Object val) {
		connector.writeValue(getModel(), val);
		valueModified();
	}

}
