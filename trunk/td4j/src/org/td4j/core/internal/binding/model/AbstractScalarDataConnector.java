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

package org.td4j.core.internal.binding.model;

import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.internal.binding.model.converter.DefaultConverterRepository;
import org.td4j.core.internal.binding.model.converter.IConverter;

public abstract class AbstractScalarDataConnector extends AbstractDataConnector implements IScalarDataConnector {

	protected AbstractScalarDataConnector(Class<?> modelType, Class<?> valueType) {
		super(modelType, valueType);
	}

	public Object readValue(Object model) {
		if (model == null || ! canRead(model)) return null;

		try {
			return readValue0(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void writeValue(Object model, Object val) {
		if (model == null || ! canWrite(model)) throw new IllegalStateException("not writable");

		try {
			writeValue0(model, val);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// TODO: implementation shall throw NPE if model==null -> check all subclasses
	protected abstract Object readValue0(Object model) throws Exception;

	protected abstract void writeValue0(Object model, Object val) throws Exception;

	

	public ScalarDataProxy createProxy() {
	  
	  // PEND: fix this, temporary only conversion to String supported !!
	  final Class<?> fromType = getType();
	  final Class<?> toType = String.class;
	  final IConverter converter = DefaultConverterRepository.INSTANCE.getConverter(fromType, toType);
	  
	  return createProxy(converter);
	}
	
	public ScalarDataProxy createProxy(IConverter converter) {
	  return new ScalarDataProxy(this, getName(), converter);
	}

}
