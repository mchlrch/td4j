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

package org.td4j.examples.userguide;

import org.td4j.core.reflect.Hide;
import org.td4j.core.reflect.Show;
import org.td4j.swing.workbench.Workbench;


public class PropertiesAnnotations {

	public static void main(String[] args) {
		final Person johnny = new Person("Johnny", "Mnemonic", "m0lly++", "confidental: there is no spoon.");

		Workbench.start(johnny);
	}


	// --------------------------------------

	public static class Person {

		public  String  firstName;
		private String  lastName;		
		private String  password;		
		
		@Show
		private String  secretData;

		public Person(String firstName, String lastName, String password, String secretData) {
			this.firstName  = firstName;
			this.lastName   = lastName;
			this.password   = password;
			this.secretData = secretData;
		}
		
		@Hide
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		@Hide
		public int getPasswordHash() {
			return password != null ? password.hashCode() : -1;
		}
	}

}
