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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class NamedItemsContainer<T> extends OrderedContainer<T> {
	
	final Map<String, T> dictionary = new HashMap<String, T>();
	
	NamedItemsContainer(List<T> items) {
		super(items);
		
		for (T item : items) {
			final String name = nameOfItem(item);
			
			if (dictionary.containsKey(name)) {
				throw new IllegalStateException("duplicate name: " + name);
			} else {
				dictionary.put(name, item);
			}
		}
	}
	
	public T getByName(String name) {
		return dictionary.get(name);
	}
	
	protected abstract String nameOfItem(T item);

}
