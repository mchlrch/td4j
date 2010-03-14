/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2010 Michael Rauch

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

import org.td4j.core.tk.IFilter;
import org.td4j.core.tk.ObjectTK;


public class ChangeEventFilter implements IFilter<ChangeEvent> {

	private final Object src;
	private final ChangeEvent.Type type;

	public ChangeEventFilter(Object src, ChangeEvent.Type type) {
		this.src = ObjectTK.enforceNotNull(src, "src");
		this.type = ObjectTK.enforceNotNull(type, "type");
	}

	public boolean accept(ChangeEvent element) {
		return src.equals(element.getSource()) && type == element.getType();
	}

	@Override
	public String toString() {
		return src.getClass().getName() + ", " + type;
	}
	
}
