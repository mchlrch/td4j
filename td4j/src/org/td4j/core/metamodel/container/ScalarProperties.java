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

package org.td4j.core.metamodel.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.td4j.core.reflect.ScalarProperty;

public class ScalarProperties {
	
	final List<ScalarProperty> properties;
	
	public ScalarProperties(List<ScalarProperty> props) {
		if (props == null || props.isEmpty()) {
			this.properties = Collections.emptyList();
		} else {
			this.properties = new ArrayList<ScalarProperty>(props);
		}
	}

	public List<ScalarProperty> get() {
		return properties;
	}

}
