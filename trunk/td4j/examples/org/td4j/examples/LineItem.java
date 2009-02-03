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

public class LineItem {

	public Order order;
	public int quantity;
	public Article article;

	LineItem(Order order, int quantity, Article article) {
		if (order == null) throw new NullPointerException("order");
		if (quantity <= 0) throw new IllegalArgumentException("quantity <= 0");
		if (article == null) throw new NullPointerException("article");

		this.order = order;
		this.quantity = quantity;
		this.article = article;
	}

}
