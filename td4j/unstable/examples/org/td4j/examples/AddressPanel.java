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

package org.td4j.examples;

import org.td4j.core.binding.Mediator;
import org.td4j.examples.order.Address;
import org.td4j.swing.ControllerAwareGridBagPanel;
import org.td4j.swing.binding.LinkController;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.binding.WidgetBuilder;
import org.td4j.swing.workbench.Editor;


public class AddressPanel extends ControllerAwareGridBagPanel {

	public AddressPanel(Editor<Address> editor, Mediator<Address> mediator) {
		final WidgetBuilder<Address> wb = new WidgetBuilder<Address>(mediator, null, editor.getWorkbench().getNavigator());

		final LinkController person = wb.link().bindField("person");
		final TextController street = wb.text().bindMethods("street");
		final TextController zip = wb.text().bindField("zip");
		final TextController city = wb.text().bindField("city");

		addLabel(person,  0, 0, _W_, _none);
		addWidget(person, 1, 0, 3, 1, 1.0, 0.0, _W_, _horz);
		
		addLabel(street,  0, 1, _W_, _none);
		addWidget(street, 1, 1, 3, 1, 1.0, 0.0, _W_, _horz);

		addLabel(zip,  0, 2, _W_, _none);
		addWidget(zip, 1, 2, _W_, _horz);
		
		addLabel(city,  2, 2, _W_, _none);
		addWidget(city, 3, 2, _W_, _horz);
	}

}
