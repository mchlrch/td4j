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

package org.td4j.core.reflect;

import java.util.List;

import org.td4j.core.internal.reflect.AbstractExecutable;
import org.td4j.core.tk.IFilter;
import org.td4j.core.tk.ListTK;


public abstract class ModelInspector {

	public abstract List<AbstractExecutable> getExecutables(Class<?> cls);

	public List<AbstractExecutable> getExecutables(Class<?> cls, IFilter<AbstractExecutable> filter) {
		final List<AbstractExecutable> baseList = getExecutables(cls);
		return ListTK.filter(baseList, filter);
	}
	
	public abstract List<ScalarProperty> getScalarProperties(Class<?> cls);
	public abstract List<ListProperty> getListProperties(Class<?> cls);	

}
