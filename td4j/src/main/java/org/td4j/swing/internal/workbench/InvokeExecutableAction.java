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

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.td4j.core.binding.Mediator;
import org.td4j.core.internal.reflect.AbstractExecutable;
import org.td4j.core.internal.reflect.InvokationParameter;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;
import org.td4j.swing.workbench.Workbench;
import org.td4j.swing.workbench.Editor.EditorContent;

import ch.miranet.commons.ObjectTK;
import ch.miranet.commons.filter.Filter;


class InvokeExecutableAction extends AbstractAction implements IObserver {
	private static final long serialVersionUID = 1L;

	private static final MediatorEventFilter mediatorEventFilter = new MediatorEventFilter();

	private final GenericEditor editor;
	private final AbstractExecutable executable;

	private InvokationParameterDialog dialog;

	InvokeExecutableAction(GenericEditor editor, AbstractExecutable executable) {
		super(executable.toString());
		this.editor = ObjectTK.enforceNotNull(editor, "editor");
		this.executable = ObjectTK.enforceNotNull(executable, "executable");

		final Mediator<?> mediator = editor.getMediator();
		mediator.addObserver(this, mediatorEventFilter);

		setEnabled();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (executable.getParameters().isEmpty()) {
				doInvoke();
	
			} else {
				if (dialog == null) {
					dialog = createDialog(executable);
				}
				dialog.setVisible(true);
				if (dialog.getOptionType() == JOptionPane.OK_OPTION) {
					doInvoke(dialog.getParameterValues());
				}
			}
			
		} finally {
			
			// force form refresh to update data from models that are not Observable
			editor.getForm().refreshFromContext();
		}
	}

	private InvokationParameterDialog createDialog(AbstractExecutable executable) {
		final List<InvokationParameter> params = executable.getParameters();
		final InvokationParameterDialog dialog = new InvokationParameterDialog(params);
		dialog.setTitle(executable.toString());
		return dialog;
	}

	void doInvoke(Object... paramValues) {
		Object[] params = paramValues;

		if ( ! executable.isStatic()) {
			params = new Object[1 + (paramValues != null ? paramValues.length : 0)];
			if (paramValues != null && paramValues.length > 0) {
				System.arraycopy(paramValues, 0, params, 1, paramValues.length);
			}

			final Object model = editor.getModel();
			params[0] = model;
		}

		final Object result = executable.invoke(params);

		// PEND: no navigation if primitive class
		if (result != null) {
		  final Class<?> resultType = result.getClass();
		  if (! Workbench.getInstance().getNavigator().isTypeNavigatable(resultType)) {
		    return;
		  }
		  
			if (Collection.class.isAssignableFrom(resultType)) {

				// PEND: bevor methode überhaupt als executable akzeptiert wird, muss
				// auf annotation für typ geprüft werden, damit wir auch richtige
				// navigation durchführen können
			  
			    final Class<?> itemType = executable.getReturnItemType();
				final EditorContent content = new EditorContent(itemType, (Collection) result);
				Workbench.getInstance().seek(content);

				// PEND: handle arrays -> make ArrayList
			} else {
				Workbench.getInstance().seek(result);
			}
		}

	}

	@Override
	public void observableChanged(ChangeEvent event) {
		setEnabled();
	}

	private void setEnabled() {
		final Object model = editor.getModel();
		setEnabled(executable.isStatic() || model != null);
	}


	private static class MediatorEventFilter implements Filter<ChangeEvent> {
		@Override
		public boolean accept(ChangeEvent event) {
			if (ChangeEvent.Type.StateChange == event.getType()) return true;
			if (ChangeEvent.Type.PropertyChange == event.getType() && event.contains("model")) return true;

			return false;
		}
	}

}
