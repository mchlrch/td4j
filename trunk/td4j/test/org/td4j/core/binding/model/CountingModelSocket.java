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

import org.td4j.core.binding.IModelSocket;
import org.td4j.core.tk.ObjectTK;


class CountingModelSocket implements IModelSocket {

	private final Class<?> modelType;

	public Object model;
	public int setModelCount;
	public int refreshFromModelCount;

	CountingModelSocket(Class<?> modelType) {
		this.modelType = ObjectTK.enforceNotNull(modelType, "modelType");
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
		setModelCount++;
	}

	public Class<?> getModelType() {
		return modelType;
	}

	public void refreshFromModel() {
		refreshFromModelCount++;
	}

	public void reset() {
		setModelCount = 0;
		refreshFromModelCount = 0;
	}
}