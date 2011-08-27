/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

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

import ch.miranet.commons.TK;


public class IndividualDataProxy extends DataProxy {

	private final IndividualDataConnector dataAccess;
	private final IConverter converter;

	public IndividualDataProxy(IndividualDataConnector dataConnector, String name) {
		this(dataConnector, name, null);
	}
	
	public IndividualDataProxy(IndividualDataConnector dataConnector, String name, IConverter converter) {
		super(name);
		this.dataAccess = TK.Objects.assertNotNull(dataConnector, "dataConnector");
		this.converter = converter;
	}

	@Override
	public Class<?> getContextType() {
		return dataAccess.getContextType();
	}
	
	public Class<?> getValueType() {
		if (converter != null) {
			return converter.getConversionTargetType();
		} else {
			return dataAccess.getValueType();
		}
	}
	
	public IConverter getConverter() {
		return converter;
	}

	public boolean canRead() {
		return dataAccess.canRead(getContext()) && (converter == null || converter.canConvert());
	}

	public boolean canWrite() {
		return dataAccess.canWrite(getContext()) && (converter == null || converter.canUnconvert());
	}

	public Object readValue() {
		final Object val = dataAccess.readValue(getContext());
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

		dataAccess.writeValue(getContext(), val);

		valueModified();
	}

	@Override
	public String toString() {
		return getName();
	}

}
