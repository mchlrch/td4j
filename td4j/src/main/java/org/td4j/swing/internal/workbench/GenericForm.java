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

import javax.swing.JPanel;

import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.reflect.ListProperty;
import org.td4j.swing.binding.WidgetBuilder;
import org.td4j.swing.workbench.Editor;
import org.td4j.swing.workbench.Form;
import org.td4j.swing.workbench.Navigator;

import ch.miranet.commons.ObjectTK;


public class GenericForm extends Form {

	private final MetaModel metaModel;

	GenericForm(Editor editor, Class<?> modelType, MetaModel model) {
		super(editor, modelType);
		this.metaModel = ObjectTK.enforceNotNull(model, "model");
	}

	@Override
	protected JPanel createForm() {
		final Navigator navigator = getEditor().getWorkbench().getNavigator();
		
		final WidgetBuilder<Object> wBuilder = new WidgetBuilder<Object>(getMediator(), metaModel, navigator);
		final GenericPanelBuilder pBuilder = new GenericPanelBuilder(wBuilder);
		final MetaClass metaClass = metaModel.getMetaClass(getContextType());
		
		// individual plugs
		for (IndividualProperty individualProperty : metaClass.getIndividualProperties()) {
			pBuilder.addIndividualProperty(individualProperty);
		}

		// list plugs
		for (ListProperty listProperty : metaClass.getListProperties()) {
			pBuilder.addListProperty(listProperty);
		}

		return pBuilder.getPanel();
	}
}
