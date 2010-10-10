/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

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

package ch.miranet.vof;

import ch.miranet.vof.VOFactory;

public class Person {

	private String name;
	private Address.Mutable address;

	public Person() {
		final VOFactory voFactory = new VOFactory();
		address = voFactory.createImplementation(Address.Mutable.class);

		// PEND: test-only, remove this code
		address.setStreet("Itchylane");
		address.setZip("95060");
		address.setCity("Santa Cruz");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address.createImmutableClone();
	}

	public void updateAddress(Address address) {
		this.address.setStreet(address.getStreet());
		this.address.setZip(address.getZip());
		this.address.setCity(address.getCity());
	}

	@Override
	public String toString() {
		return getName();
	}

}
