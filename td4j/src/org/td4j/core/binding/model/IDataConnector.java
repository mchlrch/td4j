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

package org.td4j.core.binding.model;

public interface IDataConnector {

	public Class<?> getModelType();

	public Class<?> getType();
	
	/**
	 * Optional name. Client code can choose to use or ignore this name.
	 * This is necessary to pass name information from the model to the dataProxy
	 * 
	 * @return the name or <code>null</code
	 */
	public String getName();
	
	// TODO: diese methoden werden in DataProxy durchgereicht, ev. nur dort anbieten
	// TODO: was ist besser, die methoden  von connector gleich hier direkt anzubieten oder so
	// --> der contract bleibt in beiden varianten derselbe
	public ConnectorInfo getConnectorInfo(); 
	
}
