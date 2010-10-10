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

public class ArrayTK {

	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	public static <T> T[] enforceNotEmpty(T[] array, String name) {
		if (isEmpty(array)) throw new EmptyArrayException(name);
		return array;
	}


	public static class EmptyArrayException extends IllegalArgumentException {
		private static final long serialVersionUID = 1L;

		public EmptyArrayException(String name) {
			super(String.format("Array %s is null or empty", name));
		}
	}

}
