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

package org.td4j.core.binding;

public class MediatorForwarder<T> implements IModelSocket {

	private final Mediator<T> mediator;

	public MediatorForwarder(Mediator<T> mediator) {
		this.mediator = mediator;
	}

	public MediatorForwarder(Class<T> modelType) {
		this.mediator = new Mediator<T>(modelType);
	}

	protected Mediator<T> getMediator() {
		return mediator;
	}

	public Class<?> getModelType() {
		return mediator.getModelType();
	}

	public void setModel(Object model) {
		mediator.setModel(model);
	}

	public Object getModel() {
		return mediator.getModel();
	}

	public void refreshFromModel() {
		mediator.refreshFromModel();
	}

}
