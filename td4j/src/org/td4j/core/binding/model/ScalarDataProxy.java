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

import org.td4j.core.internal.binding.model.DataProxy;
import org.td4j.core.internal.binding.model.converter.IConverter;
import org.td4j.core.internal.capability.ScalarDataAccess;
import org.td4j.core.tk.ObjectTK;


public class ScalarDataProxy extends DataProxy {

	private final ScalarDataAccess dataAccess;
	private final IConverter converter;

	public ScalarDataProxy(ScalarDataAccess dataAccess, String name) {
		this(dataAccess, name, null);
	}
	
	public ScalarDataProxy(ScalarDataAccess dataAccess, String name, IConverter converter) {
		super(name);
		this.dataAccess = ObjectTK.enforceNotNull(dataAccess, "dataAccess");
		this.converter = converter;
	}

	@Override
	public Class<?> getModelType() {
		return dataAccess.getContextType();
	}
	
	public Class<?> getValueType() {
		return dataAccess.getValueType();
	}

	public boolean canRead() {
		return dataAccess.canRead(getModel()) && (converter == null || converter.canConvert());
	}

	public boolean canWrite() {
		return dataAccess.canWrite(getModel()) && (converter == null || converter.canUnconvert());
	}

	public Object readValue() {
		final Object val = dataAccess.readValue(getModel());
		if (converter == null) {
			return val;
		} else {
			final Object convertedVal = converter.convert(val);
			return convertedVal;
		}
	}

	public void writeValue(Object val) {
		if (converter != null) {
			val = converter.unconvert(val);
		}

		dataAccess.writeValue(getModel(), val);

		valueModified();
	}

	@Override
	public String toString() {
		return getName();
	}

}
