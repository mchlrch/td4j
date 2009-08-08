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

package org.td4j.examples.ui.helloworld;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import org.td4j.examples.AppLauncher;
import org.td4j.swing.GridBagPanel;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.binding.WidgetBuilder;



public class HelloPanel extends GridBagPanel {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		AppLauncher.launch(new HelloPanel(), "HelloWorld");
	}

	private final WidgetBuilder<HelloModel> wb = new WidgetBuilder<HelloModel>(HelloModel.class);

	public HelloPanel() {
		initUI();
		wb.setModel(new HelloModel());
	}

	private void initUI() {

		// create and bind widgets
		final TextController nameController = wb.text().bindField("name");
		final JTextField nameText = nameController.getWidget();
		nameText.setColumns(24);
		final JLabel nameLabel = nameController.getLabel();

		final ListController localeController = wb.list().bindField("localeChoice");
		final JList localeChooser = localeController.getWidget();
		final JLabel localeLabel = localeController.getLabel();
		wb.selection(localeChooser).bindField("locale");

		final TextController messageController = wb.text().bindMethods("message");
		final JTextField messageText = messageController.getWidget();
		messageText.setBorder(BorderFactory.createTitledBorder("Message"));

		// add widgets to panel
		add(nameLabel,     gbc(0, 0, 1, 1, 0.0, 0.0, _W_, _none));
		add(nameText,      gbc(1, 0, 1, 1, 1.0, 0.0, _W_, _horz));
		add(localeLabel,   gbc(0, 1, 1, 1, 0.0, 0.0, _NW, _none));
		add(localeChooser, gbc(1, 1, 1, 1, 1.0, 1.0, _W_, _both));
		add(messageText,   gbc(0, 2, 2, 1, 1.0, 0.0, _W_, _horz));
	}

}
