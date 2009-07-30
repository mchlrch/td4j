/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

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

import org.td4j.core.binding.model.ConnectorInfo;
import org.td4j.core.binding.model.IDataConnector;

public abstract class AbstractDataConnector implements IDataConnector {

	private final Class<?> modelType;
	private final Class<?> valueType;
	private final ConnectorInfo connectorInfo = new ConnectorInfo();

	protected AbstractDataConnector(Class<?> modelType, Class<?> valueType) {
		if (modelType == null) throw new NullPointerException("modelType");
		if (valueType == null) throw new NullPointerException("valueType");

		this.modelType = modelType;
		this.valueType = valueType;
	}

	public Class<?> getModelType() {
		return modelType;
	}

	public Class<?> getType() {
		return valueType;
	}
	
	@Override
	public ConnectorInfo getConnectorInfo() {
		return connectorInfo;
	}

}
