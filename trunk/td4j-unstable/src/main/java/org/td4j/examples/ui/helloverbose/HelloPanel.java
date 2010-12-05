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

package org.td4j.examples.ui.helloverbose;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.ListDataConnector;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.examples.AppLauncher;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.SelectionController;
import org.td4j.swing.binding.SelectionWidget;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.internal.binding.ListModelAdapter;
import org.td4j.swing.internal.binding.ListSelectionWidgetAdapter;



public class HelloPanel extends JPanel {

	public static void main(String[] args) {
		AppLauncher.launch(new HelloPanel(), "Hello Verbose");
	}

	private final Mediator mediator = new Mediator(HelloModel.class);

	public HelloPanel() {
		initUI();
		mediator.setContext(new HelloModel());
	}

	private void initUI() {

		// create widgets
		final JTextField nameText = new JTextField(12);
		final JLabel nameLabel = new JLabel("Name");

		final JList localeChooser = new JList();
		final JLabel localeLabel = new JLabel("Locale");

		final JTextField messageText = new JTextField();
		messageText.setBorder(BorderFactory.createTitledBorder("Message"));

		// bind widgets
		final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();
		final IndividualDataConnector nameConnector = connectorFactory.createIndividualMethodConnector(HelloModel.class, "name");
		final IndividualDataProxy nameProxy = new IndividualDataProxy(nameConnector, "name");
		mediator.addContextSocket(nameProxy);
		new TextController(nameText, nameProxy);

		// PEND:
		// final ICollectionValuePlug localeChoicePlug =
		// DefaultModelInspector.createCollectionMethodPlug(HelloModel.class,
		// "localeChoice");
		final ListDataConnector localeChoiceConnector = connectorFactory.createListFieldConnector(HelloModel.class, "localeChoiceField");
		final ListDataProxy localeChoiceProxy = new ListDataProxy(localeChoiceConnector, "localeChoiceField");
		mediator.addContextSocket(localeChoiceProxy); // use LoopbackUpdateHandler
		new ListController(localeChooser, localeChoiceProxy);

		// PEND: localeChoice selection auf "locale" binden
		final IndividualDataConnector localeConnector = connectorFactory.createIndividualMethodConnector(HelloModel.class, "locale");
		final IndividualDataProxy localeProxy = new IndividualDataProxy(localeConnector, "locale");
		mediator.addContextSocket(localeProxy);
		final ListModelAdapter listModelAdapter = new ListModelAdapter(localeChooser.getModel());
		final SelectionWidget localeSelectionWidget = new ListSelectionWidgetAdapter(localeChooser);
		new SelectionController(localeChooser.getSelectionModel(), listModelAdapter, localeProxy, localeSelectionWidget);

		final IndividualDataConnector messageConnector = connectorFactory.createIndividualMethodConnector(HelloModel.class, "message");
		final IndividualDataProxy messageProxy = new IndividualDataProxy(messageConnector, "message");
		mediator.addContextSocket(messageProxy);
		new TextController(messageText, messageProxy);

		// add widgets to panel
		setLayout(new GridBagLayout());
		add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
		add(nameText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 5), 0, 0));
		add(localeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
		add(localeChooser, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(20, 10, 0, 5), 0, 0));
		add(messageText, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 5, 5, 5), 0, 0));
	}

}
