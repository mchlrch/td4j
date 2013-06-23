/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2012 Michael Rauch

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

import javax.swing.AbstractButton;

import org.td4j.core.binding.model.Caption;

import ch.miranet.commons.TK;


public class ButtonCaption implements Caption {

	private final AbstractButton button;

	public ButtonCaption(AbstractButton button) {
		this.button = TK.Objects.assertNotNull(button, "button");
	}

	public void setText(String caption) {
		button.setText(caption != null ? caption : "");
	}

	public AbstractButton getButton() {
		return button;
	}

}
