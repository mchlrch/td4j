/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2010 Michael Rauch

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
import java.util.Collections;
import java.util.List;

import org.td4j.core.reflect.Operation;
import org.td4j.core.tk.ObjectTK;

public class Order {

	public Person person;
	public String number;
	
	// TODO
//	@OperationAffinity({"addItemOrder", "editItem", "removeItem"})
	public final List<LineItem> lineItems = new ArrayList<LineItem>();

	Order(Person person) {
		this.person = ObjectTK.enforceNotNull(person, "person");
		this.number = "" + System.currentTimeMillis();
	}

	@Operation
	public void addItem(AddItemInput input) {
		addItem(input.article, input.quantity);
	}
	
	public Order addItem(Article article, int quantity) {
		lineItems.add(new LineItem(this, quantity, article));
		return this;
	}
	
	@Operation
	public void removeItem(LineItem item) {
		lineItems.remove(item);
	}
	
	@Operation
	public void editItem(EditItemInput input) {
		final LineItem item = input.getItem();
		item.quantity = input.quantity;
	}
	
	@Override
	public String toString() {
		return number;
	}
	
	
	// ----------------------------------------------------------
	public static class AddItemInput {
		public int quantity;
		public Article article;
		private final Order order;
		
		public AddItemInput(Order order) {
			this.order = order;
		}
		
		public Order getOrder() {
			return order;
		}
		
		public List<Article> getArticleOptions() {
			// PEND: get articles from repository 
			return Collections.emptyList();
		}
	}
	
	public static class EditItemInput extends AddItemInput {
		private final LineItem item;
		
		public EditItemInput(Order order, LineItem item) {
			super(order);
			this.item = item;
		}
		
		public LineItem getItem() {
			return item;
		}
		
		// make article read-only
		public Article getArticle() {
			return article;
		}
	}

}
