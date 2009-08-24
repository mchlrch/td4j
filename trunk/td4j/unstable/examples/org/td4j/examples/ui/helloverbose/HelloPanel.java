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
import org.td4j.core.binding.model.CollectionDataProxy;
import org.td4j.core.binding.model.DefaultDataConnectorFactory;
import org.td4j.core.binding.model.ICollectionDataConnector;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.examples.AppLauncher;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.SelectionController;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.internal.binding.ListModelAdapter;



public class HelloPanel extends JPanel {

	public static void main(String[] args) {
		AppLauncher.launch(new HelloPanel(), "Hello Verbose");
	}

	private final Mediator mediator = new Mediator(HelloModel.class);

	public HelloPanel() {
		initUI();
		mediator.setModel(new HelloModel());
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
		final IDataConnectorFactory connectorFactory = new DefaultDataConnectorFactory();
		final IScalarDataConnector nameConnector = connectorFactory.createScalarMethodConnector(HelloModel.class, "name");
		final ScalarDataProxy nameProxy = nameConnector.createProxy();
		mediator.addModelSocket(nameProxy);
		new TextController(nameText, nameProxy);

		// PEND:
		// final ICollectionValuePlug localeChoicePlug =
		// DefaultModelInspector.createCollectionMethodPlug(HelloModel.class,
		// "localeChoice");
		final ICollectionDataConnector localeChoiceConnector = connectorFactory.createCollectionFieldConnector(HelloModel.class, "localeChoiceField");
		final CollectionDataProxy localeChoiceProxy = localeChoiceConnector.createProxy();
		mediator.addModelSocket(localeChoiceProxy); // use LoopbackUpdateHandler
		new ListController(localeChooser, localeChoiceProxy);

		// PEND: localeChoice selection auf "locale" binden
		final IScalarDataConnector localeConnector = connectorFactory.createScalarMethodConnector(HelloModel.class, "locale");
		final ScalarDataProxy localeProxy = localeConnector.createProxy();
		mediator.addModelSocket(localeProxy);
		new SelectionController(localeChooser.getSelectionModel(), new ListModelAdapter(localeChooser.getModel()), localeProxy);

		final IScalarDataConnector messageConnector = connectorFactory.createScalarMethodConnector(HelloModel.class, "message");
		final ScalarDataProxy messageProxy = messageConnector.createProxy();
		mediator.addModelSocket(messageProxy);
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
