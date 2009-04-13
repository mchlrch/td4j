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
import org.td4j.core.internal.binding.model.converter.IConverter;
import org.td4j.core.internal.binding.model.converter.IConverterRepository;
import org.td4j.core.tk.ObjectTK;

public class ScalarDataProxy extends DataProxy<IScalarDataConnector> {

    private final IConverter converter;
  
	public ScalarDataProxy(IScalarDataConnector connector, String name, IConverter converter) {
		super(connector, name);
		this.converter = converter;
	}

	public boolean canRead() {
		return connector.canRead(getModel()) && (converter == null || converter.canConvert());
	}

	public boolean canWrite() {
		return connector.canWrite(getModel()) && (converter == null || converter.canUnconvert());
	}

	// PEND: use converter
	public Object readValue() {
	    final Object val = connector.readValue(getModel());
	    if (converter == null) {
	      return val;
	    } else {
	      final Object convertedVal = converter.convert(val);
	      return convertedVal;
	    }
	}

	// PEND: use converter
	public void writeValue(Object val) {
	  if (converter != null) {
	    val = converter.unconvert(val);
	  }
	  
	  connector.writeValue(getModel(), val);
		
		valueModified();
	}

}
