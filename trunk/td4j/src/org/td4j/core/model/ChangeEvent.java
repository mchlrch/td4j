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

package org.td4j.core.model;

// PEND: naming: javax.swing.event.ChangeEvent gibts schon ...
//PEND: how about supporting ListPropertyChange and MapPropertyChange?
public abstract class ChangeEvent {

	public static enum Type {
		StateChange, PropertyChange, LazyPropertyChange, Custom
	}

	public abstract Object getSource();

	public abstract Type getType();

	public abstract String[] getPropertyNames();

	public abstract boolean contains(String propertyName);

	public abstract Object getOldValue(String propertyName);

	public abstract Object getNewValue(String propertyName);

	public abstract Object getCustomPayload();
	
	// PEND: implements this to allow:: ce = changeSupport.prepare(); ... ; ce.fire();
//	public abstract void fire();

}
