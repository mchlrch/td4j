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

package org.td4j.swing.internal.workbench;

import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.reflect.ModelInspector;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.workbench.Editor;
import org.td4j.swing.workbench.IEditorFactory;
import org.td4j.swing.workbench.IFormFactory;
import org.td4j.swing.workbench.Workbench;


public class GenericEditorFactory implements IEditorFactory {

	private final ModelInspector modelInspector;
	private final IFormFactory formFactory;
	private final IDataConnectorFactory connectorFactory;

	public GenericEditorFactory(ModelInspector modelInspector, IFormFactory formFactory, IDataConnectorFactory connectorFactory) {
		this.modelInspector = ObjectTK.enforceNotNull(modelInspector, "modelInspector");
		this.formFactory = ObjectTK.enforceNotNull(formFactory, "formFactory");
		this.connectorFactory = ObjectTK.enforceNotNull(connectorFactory, "connectorFactory");
	}

	public Editor createEditor(Workbench workbench, Class<?> cls) {
		return new GenericEditor(workbench, cls, modelInspector, formFactory, connectorFactory);
	}

}