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


public class Setup {

	public void setup() {
		final Person mira = new Person("Michael", "Rauch");
		mira.at("Bärenmattweg 8", "2503", "Biel/Bienne");

		// PEND: create some articles
		final List<Article> articles = new ArrayList<Article>();
		articles.add(new Article("Coding in Java"));
		articles.add(new Article("Surfing in Java"));
		articles.add(new Article("Breew your own Java!"));

		mira.order().addItem(articles.get(0), 1).addItem(articles.get(2), 2);

	}
}
