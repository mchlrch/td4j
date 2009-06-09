package org.td4j.examples.personmultiaddress;

import org.td4j.core.reflect.Executable;
import org.td4j.swing.workbench.Workbench;


public class PersonMultiAddressExample {

	public static void main(String[] args) {
		final Person homer = new Person("Homer", "Simpson");
		homer.setAddress("742 Evergreen Terrace", "99001", "Springfield");

		Workbench.start(homer, Person.class, Address.class);
	}


	// --------------------------------------

	public static class Person {

	  public int age = 30;
      public String firstName;
      public String lastName;
      public Address address;

		@Executable(paramNames = { "firstname", "lastName" })
		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		@Executable(paramNames = { "street", "zip", "city" })
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
