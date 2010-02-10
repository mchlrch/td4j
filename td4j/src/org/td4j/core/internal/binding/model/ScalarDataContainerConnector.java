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

package org.td4j.core.internal.binding.model;

import org.td4j.core.binding.model.ScalarDataContainer;
import org.td4j.core.tk.ObjectTK;


public class ScalarDataContainerConnector extends AbstractScalarDataConnector {

	public ScalarDataContainerConnector(Class<?> type) {
		super(ScalarDataContainer.class, type);
	}

	public boolean canRead(Object model) {
		return model != null && modelAsContainer(model).canRead();
	}

	public boolean canWrite(Object model) {
		return model != null && modelAsContainer(model).canWrite();
	}

	@Override
	protected Object readValue0(Object model) throws Exception {
		ObjectTK.enforceNotNull(model, "model");
		return modelAsContainer(model).getContent();
	}

	@Override
	protected void writeValue0(Object model, Object val) throws Exception {
		ObjectTK.enforceNotNull(model, "model");
		modelAsContainer(model).setContent(val);
	}

	@Override
	public String toString() {
		return getClass().getName() + ": valueType=" + getModelType();
	}

	protected ScalarDataContainer<Object> modelAsContainer(Object model) {
		@SuppressWarnings("unchecked")
		final ScalarDataContainer<Object> container = (ScalarDataContainer<Object>) model;

		return container;
	}

}