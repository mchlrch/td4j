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

package org.td4j.swing.binding;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.tk.ObjectTK;


public class ButtonController extends IndividualSwingWidgetController<AbstractButton> {

	private AbstractButton widget;

	private ActionListener actionListener = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			updateModel();
		};
	};

	public ButtonController(AbstractButton widget, IndividualDataProxy proxy) {
		super(proxy);
		this.widget = ObjectTK.enforceNotNull(widget, "widget");
		
		widget.addActionListener(actionListener);

		setAccess();
		updateView();
	}

	protected void updateView0(Object newValue) {
		final boolean selected = newValue instanceof Boolean && ((Boolean) newValue).booleanValue();

		widget.setSelected(selected);
	}

	protected Object updateModel0() {
		return widget.isSelected();
	}

	@Override
	public AbstractButton getWidget() {
		return widget;
	}

	@Override
	protected void setAccess() {
		widget.setEnabled(canWrite());
	}

}
