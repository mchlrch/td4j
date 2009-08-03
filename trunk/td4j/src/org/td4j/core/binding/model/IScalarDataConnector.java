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

import org.td4j.core.internal.binding.model.converter.IConverter;

public interface IScalarDataConnector extends IDataConnector {

	// TODO: implementation shall throw NPE if model==null -> check all subclasses
	public Object readValue(Object model);

	public void writeValue(Object model, Object val);

	// TODO: implementation shall return null if model==null -> check all
	// subclasses
	public boolean canRead(Object model);

	public boolean canWrite(Object model);

	public ScalarDataProxy createProxy();
	
	public ScalarDataProxy createProxy(IConverter converter);

}
