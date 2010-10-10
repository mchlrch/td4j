/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2010 Michael Rauch

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

package org.td4j.core.internal.metamodel;

import org.td4j.core.tk.ObjectTK;

class StatefulJavaMetaClass {
	
	static enum State {ShallowMetaClass, ShallowFeatures, QualifiedFeatures, QualifiedMetaClass};
	
	private final JavaMetaClass<?> metaClass;
	private State state;
	
	StatefulJavaMetaClass(JavaMetaClass<?> metaClass, State state) {
		this.metaClass = ObjectTK.enforceNotNull(metaClass, "metaClass");
		setState(state);
	}
	
	JavaMetaClass<?> getMetaClass() {
		return metaClass;
	}
	
	State getState() {
		return state;
	}
	
	void setState(State state) {
		this.state = ObjectTK.enforceNotNull(state, "state");
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", getState(), getMetaClass());
	}
	
}
