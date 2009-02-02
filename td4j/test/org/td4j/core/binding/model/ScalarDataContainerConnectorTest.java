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

package org.td4j.core.binding.model;

import org.td4j.core.binding.model.ScalarDataContainer;
import org.td4j.core.internal.binding.model.ScalarDataContainerConnector;
import org.testng.annotations.Test;


public class ScalarDataContainerConnectorTest {

	@Test
	public void testReadWrite() {
		final ScalarDataContainer<String> container = new ScalarDataContainer<String>(String.class, "foo");
		final ScalarDataContainerConnector con = new ScalarDataContainerConnector(container.getContentType(), container.getPropertyName());

		assert container.getContent() == null;
		assert con.readValue(container) == null;

		con.writeValue(container, "bar");
		assert "bar".equals(container.getContent());
		assert "bar".equals(con.readValue(container));

		con.writeValue(container, "baz");
		assert "baz".equals(container.getContent());
		assert "baz".equals(con.readValue(container));

		con.writeValue(container, null);
		assert container.getContent() == null;
		assert con.readValue(container) == null;
	}

	// TODO: testReadOnlyContainer

}
