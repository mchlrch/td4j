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

package org.td4j.core.binding.model;

import org.td4j.core.binding.model.IndividualDataContainer;
import org.td4j.core.model.CountingObserver;
import org.testng.annotations.Test;


public class IndividualDataContainerTest {

	@Test
	public void testContentChange() {
		final IndividualDataContainer<String> container = new IndividualDataContainer<String>(String.class, "foo");
		final CountingObserver observer = new CountingObserver();
		container.addObserver(observer);

		assert container.getContent() == null;
		assert observer.stateChangeCount == 0;

		container.setContent("bar");
		assert "bar".equals(container.getContent());
		assert observer.stateChangeCount == 1;

		container.setContent("bar");
		assert observer.stateChangeCount == 1;

		container.setContent("baz");
		assert "baz".equals(container.getContent());
		assert observer.stateChangeCount == 2;

		container.setContent(null);
		assert container.getContent() == null;
		assert observer.stateChangeCount == 3;
	}

}
