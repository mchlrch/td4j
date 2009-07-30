package org.td4j.examples.personmultiaddress;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Executable;
import org.td4j.core.reflect.ExposeProperties;
import org.td4j.core.reflect.ExposePropertiesInEditorList;
import org.td4j.swing.workbench.Workbench;


/**
 * This example shows the usage of the annotations  
 * @ExposePropertiesInEditorList and @ExposeProperties to configure
 * the properties shown in a table. 
 */
public class PersonMultiAddressExample {

	public static void main(String[] args) {
		final Person homer = new Person("Homer", "Simpson");
		homer.addAddress("742 Evergreen Terrace", "99001", "Springfield");

		final Person bart = new Person("Bart", "Simpson");
		final PersonRelation homerToBart = new PersonRelation(homer, bart, "is-father-of", "is-son-of");
		homer.relationsOut.add(homerToBart);
		bart.relationsIn.add(homerToBart);
		
		Workbench.start(homer, Person.class, Address.class);
	}


	// --------------------------------------

	@ExposePropertiesInEditorList({"firstName", "lastName"})
	public static class Person {

	  public int age = 30;
      public String firstName;
      public String lastName;
      
      @ExposeProperties({"street", "zip", "city"})
// TODO     @HideProperties({"person"})
      public List<Address> addresses = new ArrayList<Address>();
      
      @ExposeProperties({"humanReadableForward", "forwardRole", "to"})
      public List<PersonRelation> relationsOut = new ArrayList<PersonRelation>();
      
      @ExposeProperties({"humanReadableBackward", "backwardRole", "from"})
      public List<PersonRelation> relationsIn = new ArrayList<PersonRelation>();

		@Executable(paramNames = { "firstname", "lastName" })
		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		@Executable(paramNames = { "street", "zip", "city" })
		public Address addAddress(String street, String zip, String city) {
			final Address address = new Address(this, street, zip, city);
			addresses.add(address);
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
	
	
	public static class PersonRelation {
		public Person from;
		public Person to;
		public String forwardRole;
		public String backwardRole;
		
		PersonRelation(Person from, Person to, String forwardRole, String backwardRole) {
			this.from = from;
			this.to = to;
			this.forwardRole = forwardRole;
			this.backwardRole = backwardRole;
		}
		
		public String getHumanReadableForward() {
			return "" + from + " " + forwardRole + " " + to;
		}
		
		public String getHumanReadableBackward() {
			return "" + to + " " + backwardRole + " " + from;
		}
	}

}
