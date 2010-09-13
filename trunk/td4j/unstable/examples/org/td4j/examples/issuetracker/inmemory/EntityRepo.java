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

package org.td4j.examples.issuetracker.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.td4j.core.tk.IFilter;
import org.td4j.core.tk.filter.AcceptAllFilter;

public class EntityRepo {
	
	private final HashMap<Class<?>, List<Object>> repo = new HashMap<Class<?>, List<Object>>();
	
	public <T> void put(Class<T> cls, T entity) {
		List<Object> entities = repo.get(cls);
		if (entities == null) {
			entities = new ArrayList<Object>();
			repo.put(cls, entities);
		}
		
		if ( ! entities.contains(entity)) {
			entities.add(entity);
		}
	}
	
	public <T> void remove(Class<T> cls, T entity) {
		final List<Object> entities = repo.get(cls);
		if (entities != null) {
			entities.remove(entity);
		}
	}
	
	public <T> List<T> getAll(Class<T> cls) {
		final IFilter<T> filter = AcceptAllFilter.getInstance(); 
		return get(cls, filter);
	}
	
	public <T> List<T> get(Class<T> cls, IFilter<T> filter) {
		final List<T> result = new ArrayList<T>();
		
		final List<Object> entities = repo.get(cls);
		if (entities != null) {
			for (Object obj : entities) {
				final T entity = (T) obj;
				if (filter.accept(entity)) {
					result.add(entity);
				}
			}
		}
		
		return result;
	}

}
