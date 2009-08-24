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

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Executable;



public class Order {

	public Person person;
	public String number;
	public final List<LineItem> lineItems = new ArrayList<LineItem>();

	Order(Person person) {
		if (person == null) throw new NullPointerException("person");

		this.person = person;
		this.number = "" + System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return number;
	}

	@Executable(paramNames={"Article", "Pieces"})
	public Order addItem(Article article, int i) {
		lineItems.add(new LineItem(this, i, article));
		return this;
	}

}
