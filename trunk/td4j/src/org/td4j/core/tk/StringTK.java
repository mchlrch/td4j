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

public class StringTK {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

	public static String enforceNotEmpty(String s, String name) {
		if (isEmpty(s)) throw new EmptyStringException(name);
		return s;
	}


	public static class EmptyStringException extends IllegalArgumentException {
		private static final long serialVersionUID = 1L;

		public EmptyStringException(String name) {
			super(String.format("String %s is null or empty", name));
		}
	}

}
