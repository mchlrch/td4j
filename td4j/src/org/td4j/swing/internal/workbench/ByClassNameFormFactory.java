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

package org.td4j.swing.internal.workbench;

import java.lang.reflect.Constructor;

import javax.swing.JComponent;

import org.td4j.core.binding.Mediator;
import org.td4j.swing.workbench.Editor;
import org.td4j.swing.workbench.Form;
import org.td4j.swing.workbench.FormFactory;

import ch.miranet.commons.ObjectTK;


public class ByClassNameFormFactory implements FormFactory {

	@Override
	public Form createForm(Editor editor, Class<?> modelType) {

		try {
			final Class panelClass = Class.forName(modelType.getName() + "Panel");

			if (JComponent.class.isAssignableFrom(panelClass)) {
				final Constructor<JComponent> constructor = panelClass.getConstructor(Editor.class, Mediator.class);
				final Form form = new ByClassNameForm(editor, modelType, constructor);
				return form;
			}

		} catch (ClassNotFoundException cnfex) {
			// ignore, as implementing hand-made forms is optional
		} catch (Exception ex) {
			// notify about constructor mismatch and exception during initialization
			throw new RuntimeException(ex);
		}

		return null;
	}


	// -------------------------------------------------

	private static class ByClassNameForm<T> extends Form<T> {

		private final Constructor<JComponent> panelConstructor;

		private ByClassNameForm(Editor editor, Class<T> modelType, Constructor<JComponent> panelConstructor) {
			super(editor, modelType);
			this.panelConstructor = ObjectTK.enforceNotNull(panelConstructor, "panelConstructor");
		}

		@Override
		protected JComponent createForm() {
			JComponent form = null;

			try {
				form = panelConstructor.newInstance(getEditor(), getMediator());
				return form;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

	}

}
