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

package org.td4j.examples.personrelation;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Executable;
import org.td4j.core.reflect.ExposeProperties;
import org.td4j.core.reflect.ExposePropertiesInEditorList;
import org.td4j.swing.workbench.Workbench;

/**
 * This example shows the usage of the annotations @ExposePropertiesInEditorList and
 * @ExposeProperties to configure the properties shown in a table.
 */
public class PersonRelationExample {

	public static void main(String[] args) {
		final Person homer = new Person("Homer", "Simpson");
		final Person bart = new Person("Bart", "Simpson");
		final PersonRelation homerToBart = new PersonRelation(homer, bart, "is-father-of", "is-son-of");

		Workbench.start(homer, Person.class);
	}

	// --------------------------------------

	@ExposePropertiesInEditorList( { "firstName", "lastName" })
	public static class Person {

		public String firstName;
		public String lastName;

		@ExposeProperties( { "forwardRole", "to" })
		public List<PersonRelation> relationsOut = new ArrayList<PersonRelation>();

		@ExposeProperties( { "backwardRole", "from" })
		public List<PersonRelation> relationsIn = new ArrayList<PersonRelation>();

		@Executable(paramNames = { "firstName", "lastName" })
		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		@Override
		public String toString() {
			return "" + firstName + " " + lastName;
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

			from.relationsOut.add(this);
			to.relationsIn.add(this);
		}

		public String getHumanReadableForward() {
			return "" + from + " " + forwardRole + " " + to;
		}

		public String getHumanReadableBackward() {
			return "" + to + " " + backwardRole + " " + from;
		}
	}

}
