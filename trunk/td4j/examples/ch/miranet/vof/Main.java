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

public class Main {
	
	
	public static void main(String[] args) {
		final Person person = new Person();
		person.setName("Homer Simpsons");
		
		final Address a1 = person.getAddress();
		
		printPerson("orig", person);
		
		final Address.Mutable a2 = a1.createMutableClone();
		a2.setStreet("Evergreen Terrace");
		a2.setZip("98765");
		a2.setCity("Springfield");
		
		printPerson("external change", person);
		
		person.updateAddress(a2);
		
		printPerson("updated", person);
	}
	
	
	private static void printPerson(String title, Person person) {
		System.out.println("--[ " + title + " ]------------------------------");
		
		System.out.println(person);
		
		final Address address = person.getAddress();
		
		System.out.println(address.getStreet());
		System.out.println(address.getZip() + " " + address.getCity());
		System.out.println();
	}

}
