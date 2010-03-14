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

package org.td4j.examples.order;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.Observable;
import org.td4j.core.model.ObservableList;
import org.td4j.core.reflect.Executable;
import org.td4j.core.reflect.ExposeProperties;
import org.td4j.swing.workbench.Workbench;



/**
 * widget enabled state: - ähnlich wie in
 * http://martinfowler.com/eaaDev/PresentationModel.html beschrieben, muss logik
 * layer zwischengeschaltet werden, der infos für die präsentation aufbereitet
 * (property B editierbar, falls A == true; ISecurityAware)
 * 
 * @author mira
 * 
 */

@ExposeProperties( { "firstName", "lastName", "address", "orders" })
public class Person extends Observable {

	public String firstName;
	public String lastName;
	public String email;
	public Boolean active;
	public Address address;

	public static final List<Address> addressChoice = new ArrayList<Address>();
	public static final Person BART;

	static {
		BART = new Person("Bart", "Simpson");
		addressChoice.add(BART.at("Bärenmatt 8", "2503", "Biel/Bienne"));
		addressChoice.add(BART.at("Bartolomäusweg 22", "2503", "Biel/Bienne"));
		addressChoice.add(BART.at("Barkenhafen 1", "2502", "Biel/Bienne"));
		addressChoice.add(BART.at("Obermattweg 5", "3415", "Hasle-Rüegsau"));
		addressChoice.add(BART.at("Alpenstrasse 24", "3415", "Hasle-Rüegsau"));
		addressChoice.add(BART.at("Andermattweg 22", "3415", "Hasle-Rüegsau"));
		addressChoice.add(BART.at("Riedbachstr. 98", "3000", "Bern"));
		addressChoice.add(BART.at("Belpstrasse 15", "3000", "Bern"));
		addressChoice.add(BART.at("Rüschligasse 22", "3001", "Bern"));
		addressChoice.add(BART.at("Ackermatte 7", "3001", "Bern"));
		addressChoice.add(BART.at("Spitalstr. 54", "3001", "Bern"));
		addressChoice.add(BART.at("Werkgasse 11", "3002", "Bern"));
	}

	private final List<Order> orders = new ObservableList<Order>(new ArrayList<Order>(), changeSupport, "orders");

	@Executable(paramNames={"firstname", "lastName"})
	public Person(String firstName, String lastName) {
		if (firstName == null) throw new NullPointerException("firstName");
		if (lastName == null) throw new NullPointerException("lastName");

		this.firstName = firstName;
		this.lastName = lastName;
		this.active = true;
	}

	@Executable(paramNames={"street", "zip", "city"})
	public Address at(String street, String zip, String city) {
		final Address newAddress = new Address(this, street, zip, city);
		final ChangeEvent event = changeSupport.preparePropertyChange("address", this.address, newAddress);
		this.address = newAddress;
		changeSupport.fire(event);
		return address;
	}

	@Executable
	public Order order() {
		final Order order = new Order(this);
		orders.add(order);
		return order;
	}

	// PEND: indexedPropertyTest only
	public String getPrefixedName(String prefix) {
		return "" + prefix + " " + firstName;
	}

	public void setPrefixedName(String prefix, String name) {
	}

	// PEND: test only, DefaultModelInspector
	// public String getFirstName() { return "1st-Name"; }
	// public String getZeroName() { return "zero-Name"; }

	@Override
	public String toString() {
		return "" + firstName + " " + lastName;
	}
	
	@Executable
	public void printName(Boolean uppercase) {
		final String printName = uppercase != null && uppercase.booleanValue() ? toString().toUpperCase() : toString();
		System.out.println(printName);
	}
	
	@Executable
	public static void printClass() {
		System.out.println(Person.class.getName());
	}
	
	@Executable(paramNames={"head", "tail"})
	public void concat(String s1, String s2) {
		System.out.println("" + s1 + " " + s2);
	}
	
	
	@Executable
	public void clearAddress() {
		final ChangeEvent event = changeSupport.preparePropertyChange("address", this.address, null);
		if (event == null) return;
		
		this.address = null;
		changeSupport.fire(event);
	}	


	
	public static void main(String[] args) {
		// PEND: nur testmodel
		final Person homer = new Person("Homer", "Simpson");
		homer.at("742 Evergreen Terrace", "99001", "Springfield");

		Workbench.start(homer, Person.class, Address.class);
	}
}
