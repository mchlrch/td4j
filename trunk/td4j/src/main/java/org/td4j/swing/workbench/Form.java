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

package org.td4j.swing.workbench;

import javax.swing.JComponent;

import org.td4j.core.binding.MediatorForwarder;

import ch.miranet.commons.TK;


public abstract class Form extends MediatorForwarder<Object> {

	private final Editor editor;

	private JComponent form;

	protected Form(Editor editor, Class<?> ctxType) {
		super(ctxType);
		this.editor = TK.Objects.assertNotNull(editor, "editor");
	}

	public JComponent getComponent() {
		if (this.form == null) {
			this.form = createForm();
			TK.Objects.assertNotNull(form, "form");
		}

		return form;
	}

	public Editor getEditor() {
		return editor;
	}

	protected abstract JComponent createForm();

}
