/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

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

package org.td4j.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.ListDataConnector;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.binding.model.IndividualDataRelay;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.examples.order.Address;
import org.td4j.examples.order.Person;
import org.td4j.swing.binding.ButtonController;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.binding.WidgetBuilder;



public class MainUISwing extends ControllerAwareGridBagPanel {
	private static final long serialVersionUID = 1L;

	MainUISwing() {
		final JLabel nameLabel = new JLabel("firstName");
		final JTextField nameText = new JTextField("-name-");

		final JLabel name2Label = new JLabel("firstName");
		final JTextField name2Text = new JTextField("-name2-");
		
		add(nameLabel,  0, 0, _W_, _none);
		add(nameText,   1, 0, _W_, _horz);
		add(name2Label, 0, 1, _W_, _none);
		add(name2Text,  1, 1, _W_, _horz);

		final Person homer = new Person("Homer", "Simpson");
		homer.at("Evergreen Terrace", "98765", "Springfield");

		final DataConnectorFactory conFactory = new JavaDataConnectorFactory();

		// direct
		final IndividualDataConnector nameFieldPlug = conFactory.createIndividualFieldConnector(Person.class, "firstName");
		final IndividualDataProxy nameFieldProxy = new IndividualDataProxy(nameFieldPlug, "firstName");
		new TextController(nameText, nameFieldProxy);
		nameFieldProxy.setContext(homer);

		// with mediator
		final IndividualDataConnector name2FieldPlug = conFactory.createIndividualFieldConnector(Person.class, "firstName");
		final Mediator<Person> mediator = new Mediator<Person>(Person.class);
		final IndividualDataProxy name2FieldProxy = new IndividualDataProxy(name2FieldPlug, "firstName");
		mediator.addContextSocket(name2FieldProxy);
		new TextController(name2Text, name2FieldProxy);
		mediator.setContext(homer);

		// WidgetBuilder
		final WidgetBuilder<Person> builder = new WidgetBuilder<Person>(mediator, null, null);
		// final JTextField name3Text = builder.text().bindField("firstName");
		// add(builder.lcl(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
		// GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		// add(name3Text, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
		// GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		final TextController firstNameController = builder.text().bindField("firstName");
		addLabel(firstNameController,  0, 2, _W_, _none);
		addWidget(firstNameController, 1, 2, _W_, _horz);

		final JLabel activeButtonCaption = new JLabel(":A:C:T:I:V:E:");
		final AbstractButton activeButton = builder.button().bindField("active").getWidget();
		final JLabel active2Caption = new JLabel();
		active2Caption.setBackground(Color.red);		
		final ButtonController active2Controller = builder.caption(active2Caption).button(new JRadioButton()).bindField("active");

		add(activeButtonCaption,  0, 3, _W_, _none);
		add(activeButton,         1, 3, _W_, _horz);
		addLabel(active2Controller,  0, 4, _W_, _none);
		addWidget(active2Controller, 1, 4, _W_, _horz);

		final IndividualDataConnector indexedConnector = conFactory.createIndividualMethodConnector(Person.class, "prefixedName", new Class[] { String.class }, new Object[] { "Mr." });
		final TextController indexedTextController = builder.text().bindConnector(indexedConnector, "prefixedName"); 
		addLabel(indexedTextController,  0, 5, _W_, _none);
		addWidget(indexedTextController, 1, 5, _W_, _horz);

		// cascaded properties: person.address.city
		final IndividualDataConnector addressConnector = conFactory.createIndividualFieldConnector(Person.class, "address");
		final IndividualDataProxy addressProxy          = new IndividualDataProxy(addressConnector, "address");
		final IndividualDataConnector cityConnector    = conFactory.createIndividualFieldConnector(Address.class, "city");
		final IndividualDataProxy cityProxy             = new IndividualDataProxy(cityConnector, "city");
		new IndividualDataRelay(addressProxy, cityProxy);
		final JLabel cityLabel = new JLabel("city(cascaded)");
		final JTextField cityText = new JTextField("-city-");
		new TextController(cityText, cityProxy);
		addressProxy.setContext(homer);

		add(cityLabel, 0, 6, _W_, _none);
		add(cityText,  1, 6, _W_, _horz);

		// list: direct mit proxy
		final JList orderList = new JList();
		final ListDataConnector orderListConnector = conFactory.createListFieldConnector(Person.class, "orders");
		final ListDataProxy orderListProxy = new ListDataProxy(orderListConnector, "orders");
		new ListController(orderList, orderListProxy);
		mediator.addContextSocket(orderListProxy); // use LoopbackUpdateHandler

		final Action addOrderAction = new AbstractAction("addOrder") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				homer.order();
			}
		};
		final JButton addOrderButton = new JButton(addOrderAction);
		add(addOrderButton, 0, -1, _C_, _none);
		add(orderList,      1, -1, _W_, _both);

		// PEND: siehe PENDS in MainUISWT
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final MainUISwing panel = new MainUISwing();
				final JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
