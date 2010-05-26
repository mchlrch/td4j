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

import java.awt.Component;

import javax.swing.JLabel;

import org.td4j.swing.binding.ListSwingWidgetController;
import org.td4j.swing.binding.IndividualSwingWidgetController;


public class ControllerAwareGridBagPanel extends GridBagPanel {
	private static final long serialVersionUID = 1L;

	public void addLabel(IndividualSwingWidgetController<?> controller, int gridx, int gridy, int anchor, int fill) {
		final JLabel label = controller.getLabel();
		add(label, gbc(gridx, gridy, anchor, fill));
	}

	public void addLabel(IndividualSwingWidgetController<?> controller, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {
		final JLabel label = controller.getLabel();
		add(label, gbc(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill));
	}

	public void addLabel(ListSwingWidgetController<?> controller, int gridx, int gridy, int anchor, int fill) {
		final JLabel label = controller.getLabel();
		add(label, gbc(gridx, gridy, anchor, fill));
	}

	public void addLabel(ListSwingWidgetController<?> controller, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {
		final JLabel label = controller.getLabel();
		add(label, gbc(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill));
	}

	// ========================================

	public void addWidget(IndividualSwingWidgetController<?> controller, int gridx, int gridy, int anchor, int fill) {
		final Component widget = controller.getWidget();
		add(widget, gbc(gridx, gridy, anchor, fill));
	}

	public void addWidget(IndividualSwingWidgetController<?> controller, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {
		final Component widget = controller.getWidget();
		add(widget, gbc(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill));
	}

	public void addWidget(ListSwingWidgetController<?> controller, int gridx, int gridy, int anchor, int fill) {
		final Component widget = controller.getWidget();
		add(widget, gbc(gridx, gridy, anchor, fill));
	}

	public void addWidget(ListSwingWidgetController<?> controller, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {
		final Component widget = controller.getWidget();
		add(widget, gbc(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill));
	}

}
