/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009, 2010 Michael Rauch

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

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Operation;
import org.td4j.core.reflect.ShowProperties;


/**
 * This is the example from the 'Getting Started Guide'.
 * 
 * It's based on {@link PersonExample} and has some adaptions
 * to show additional features.
 */
public class PersonExample2 {
	
	public static void main(String[] args) {
		Person homer = new Person("Homer", "Simpson");
		homer.addAddress("742 Evergreen Terrace", "99001", "Springfield");

		org.td4j.swing.workbench.Workbench.start(homer, Person.class, Address.class);
	}
	
	
	public static class Person {
		
		public String firstName;
		public String lastName;
		
		@ShowProperties({"street", "zip", "city"})
		public List<Address> addresses = new ArrayList<Address>();
		
		@Operation(paramNames = { "firstName", "lastName" })
		Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}		
		
		@Operation(paramNames = { "street", "zip", "city" })
		public void addAddress(String street, String zip, String city) {
			final Address address = new Address(this, street, zip, city);
			addresses.add(address);
		}
		
		public String toString() {
			return "" + firstName + " " + lastName;
		}

		public void removeAddress(Address address) {
			addresses.remove(address);			
		}
	}
	
	
	public static class Address {

		private Person person;
		private String street;
		private String zip;
		private String city;

		Address(Person person, String street, String zip, String city) {
			this.person = person;
			this.street = street;
			this.zip = zip;
			this.city = city;
		}
		
		public Person getPerson() {	return person; }
		public String getStreet() {	return street; }
		public String getZip() {		return zip;    }
		public String getCity() {		return city;   }
		
		@Operation
		public void delete() {
			person.removeAddress(this);
			this.person = null;
		}
		
		public String toString() {
			return "" + street + ", " + zip + " " + city;
		}
	}
	
	


}
