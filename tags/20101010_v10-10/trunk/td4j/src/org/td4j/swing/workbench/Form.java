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
import org.td4j.core.tk.ObjectTK;


public abstract class Form<T> extends MediatorForwarder<T> {

	private final Editor editor;

	private JComponent form;

	protected Form(Editor editor, Class<T> ctxType) {
		super(ctxType);
		this.editor = ObjectTK.enforceNotNull(editor, "editor");
	}

	public JComponent getComponent() {
		if (this.form == null) {
			this.form = createForm();
			ObjectTK.enforceNotNull(form, "form");
		}

		return form;
	}

	public Editor getEditor() {
		return editor;
	}

	protected abstract JComponent createForm();

}
