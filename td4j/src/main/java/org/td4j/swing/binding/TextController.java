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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import org.td4j.core.binding.model.IndividualDataProxy;

import ch.miranet.commons.TK;


public class TextController extends IndividualSwingWidgetController<JTextField> {

	private final JTextField widget;

	private final KeyListener enterListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			if (KeyEvent.VK_ENTER == e.getKeyCode()) {
				updateModel();
			}
		}
	};

	private final FocusListener focusListener = new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			updateModel();
		}
	};
	

	public TextController(JTextField widget, IndividualDataProxy proxy) {
		this(widget, proxy, true);
	}

	public TextController(JTextField widget, IndividualDataProxy proxy, boolean registerListeners) {
		super(proxy);

		this.widget = TK.Objects.assertNotNull(widget, "widget");

		// PEND: test mode only - ChoiceUI
		if (registerListeners) {
			widget.addKeyListener(enterListener);
			widget.addFocusListener(focusListener);
		}

		setAccess();
		updateView();
	}
	
	public JTextField getWidget() {
		return widget;
	}

	
	protected void setAccess() {
		widget.setEnabled(canRead());
		widget.setEditable(canWrite());
	}
	
	protected void updateView0(Object newValue) {
		widget.setText(newValue != null ? newValue.toString() : "");
	}

	protected Object readView0() {
		return widget.getText();
	}

}
