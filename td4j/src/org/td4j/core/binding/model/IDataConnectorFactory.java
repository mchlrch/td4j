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

import java.util.List;

import org.td4j.core.reflect.PendingConnectorInfo;



public interface IDataConnectorFactory {

	
	// TODO: handle infoQueue correctly
	
	public IDataConnector createConnector(Class<?> cls, String name);
	public IDataConnector createConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue);
	
	public IScalarDataConnector createScalarFieldConnector(Class<?> cls, String name);
	public IScalarDataConnector createScalarFieldConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue);
	
	public ICollectionDataConnector createCollectionFieldConnector(Class<?> cls, String name);
	public ICollectionDataConnector createCollectionFieldConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue);
	
	public IDataConnector createFieldConnector(Class<?> cls, String name);
	public IDataConnector createFieldConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue);

	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name);
	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue);
	
	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues);
	public IScalarDataConnector createScalarMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues, List<PendingConnectorInfo> infoQueue);
	
	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name);
	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue);

	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues);
	public ICollectionDataConnector createCollectionMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues, List<PendingConnectorInfo> infoQueue);
	
	public IDataConnector createMethodConnector(Class<?> cls, String name);
	public IDataConnector createMethodConnector(Class<?> cls, String name, List<PendingConnectorInfo> infoQueue);

	public IDataConnector createMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues);
	public IDataConnector createMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues, List<PendingConnectorInfo> infoQueue);
	
}
