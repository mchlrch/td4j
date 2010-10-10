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

package org.td4j.core.tk;

import java.util.ArrayList;
import java.util.List;

// PEND: what about CollectionTK ?
public class ListTK {

	public static <T> List<T> filter(List<T> baseList, IFilter<T> filter) {
		final ArrayList<T> result = new ArrayList<T>();
		for (T element : baseList) {
			if (filter.accept(element)) {
				result.add(element);
			}
		}
		return result;
	}
	
	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}

	public static <T> List<T> enforceNotEmpty(List<T> list, String name) {
		if (isEmpty(list)) throw new EmptyListException(name);
		return list;
	}


	public static class EmptyListException extends IllegalArgumentException {
		private static final long serialVersionUID = 1L;

		public EmptyListException(String name) {
			super(String.format("List %s is null or empty", name));
		}
	}


}
