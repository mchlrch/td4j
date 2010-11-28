/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2010 Michael Rauch

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

import org.td4j.core.internal.binding.model.ListFieldConnector;
import org.td4j.core.internal.binding.model.ListMethodConnector;
import org.td4j.core.internal.binding.model.IndividualFieldConnector;
import org.td4j.core.internal.binding.model.IndividualMethodConnector;
import org.td4j.core.reflect.DataConnector;

public interface DataConnectorFactory {

	public DataConnector createConnector(Class<?> cls, String name);
	
	public IndividualFieldConnector createIndividualFieldConnector(Class<?> cls, String name);
	
	public ListFieldConnector createListFieldConnector(Class<?> cls, String name);
	
	public DataConnector createFieldConnector(Class<?> cls, String name);

	public IndividualMethodConnector createIndividualMethodConnector(Class<?> cls, String name);
	
	public IndividualMethodConnector createIndividualMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues);
	
	public ListMethodConnector createListMethodConnector(Class<?> cls, String name);

	public ListMethodConnector createListMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues);
	
	public DataConnector createMethodConnector(Class<?> cls, String name);

	public DataConnector createMethodConnector(Class<?> cls, String name, Class<?>[] argumentTypes, Object[] argumentValues);
	
}
