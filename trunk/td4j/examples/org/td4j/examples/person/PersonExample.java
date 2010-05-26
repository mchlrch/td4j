/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

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

package org.td4j.examples.person;

import org.td4j.core.reflect.Operation;
import org.td4j.swing.workbench.Workbench;


public class PersonExample {

	public static void main(String[] args) {
		final Person homer = new Person("Homer", "Simpson");
		homer.setAddress("742 Evergreen Terrace", "99001", "Springfield");

		Workbench.start(homer, Person.class, Address.class);
	}


	// --------------------------------------

	public static class Person {

		public String firstName;
		public String lastName;
		public Address address;
		
		// TODO: birthdate als PrimitiveType implementieren - toString / fromString

		@Operation(paramNames = { "firstname", "lastName" })
		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		@Operation(paramNames = { "street", "zip", "city" })
		public Address setAddress(String street, String zip, String city) {
			address = new Address(this, street, zip, city);
			return address;
		}

		@Override
		public String toString() {
			return "" + firstName + " " + lastName;
		}
	}


	// --------------------------------------

	public static class Address {

		public Person person;
		public String street;
		public String zip;
		public String city;

		Address(Person person, String street, String zip, String city) {
			this.person = person;
			this.street = street;
			this.zip = zip;
			this.city = city;
		}

		@Override
		public String toString() {
			return "" + street + ", " + zip + " " + city;
		}
	}

}
