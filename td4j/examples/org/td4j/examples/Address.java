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

package org.td4j.examples;

import java.util.List;

import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.Observable;
import org.td4j.core.reflect.Executable;
import org.td4j.core.reflect.ExposeProperties;


@ExposeProperties
public class Address extends Observable {

	public Person person;
	public String street;
	public String zip;
	public String city;

	@Executable
	public static List<Address> findAddresses() {
		return Person.addressChoice;
	}

	Address(Person person, String street, String zip, String city) {
		if (person == null) throw new NullPointerException("person");
		if (street == null) throw new NullPointerException("street");
		if (zip == null) throw new NullPointerException("zip");
		if (city == null) throw new NullPointerException("city");

		this.person = person;
		this.street = street;
		this.zip = zip;
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		final ChangeEvent event = changeSupport.preparePropertyChange("street", this.street, street);
		if (event == null) return;

		this.street = street;
		changeSupport.fire(event);
	}

	@Override
	public String toString() {
		return "" + street + ", " + zip + " " + city;
	}

	public String getFoo() {
		return "bar";
	}

	@Executable
	public void printPerson() {
		System.out.println(person);
	}

	@Executable
	public static void printClass() {
		System.out.println(Address.class.getName());
	}

}
