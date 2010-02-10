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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.internal.binding.model.converter.DefaultConverterRepository;
import org.td4j.core.internal.binding.model.converter.IConverter;
import org.td4j.core.reflect.ListProperty;
import org.td4j.core.reflect.ModelInspector;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.binding.ButtonController;
import org.td4j.swing.binding.LabelController;
import org.td4j.swing.binding.LinkController;
import org.td4j.swing.binding.TableController;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.binding.WidgetBuilder;
import org.td4j.swing.internal.binding.TableControllerFactory;
import org.td4j.swing.workbench.Editor;
import org.td4j.swing.workbench.Form;


public class GenericForm<T> extends Form<T> {

	private final ModelInspector modelInspector;

	GenericForm(Editor editor, Class<T> modelType, ModelInspector modelInspector) {
		super(editor, modelType);
		this.modelInspector = ObjectTK.enforceNotNull(modelInspector, "modelInspector");
	}

	@Override
	protected JPanel createForm() {
		final JPanel panel = new JPanel(new GridBagLayout());

		// scalar plugs
		final WidgetBuilder<Object> wBuilder = new WidgetBuilder<Object>(getMediator(), getEditor().getWorkbench().getNavigator());
		for (ScalarProperty scalarProperty : modelInspector.getScalarProperties(getModelType())) {
			final Class<?> type = scalarProperty.getValueType();

			// PEND: das kreieren der widgets (+controller) muss auch pluggable sein,
			// damit neue controller typen unterstütz werden können

			// PEND: fix this, code duplication with AbstractScalarDataConnector, InvokationParameterDialog
			// PEND: fix this, temporary only conversion to String supported !!
		      final Class<?> fromType = type;
		      final Class<?> toType = String.class;
		      final IConverter converter = DefaultConverterRepository.INSTANCE.getConverter(fromType, toType);
			final boolean convertableToString = converter != null;
			
			final JLabel label = new JLabel();
			panel.add(label, new GridBagConstraints(0, - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
			final ScalarDataProxy scalarProxy = new ScalarDataProxy(scalarProperty, scalarProperty.getName());
			getMediator().addModelSocket(scalarProxy);
			if (type == Boolean.class || type == boolean.class) {
				final ButtonController btnController = wBuilder.caption(label).button().bind(scalarProxy);
				final AbstractButton btn = btnController.getWidget();
				panel.add(btn, new GridBagConstraints(1, - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			} else if (type == String.class || convertableToString) {
				final TextController textController = wBuilder.caption(label).text().bind(scalarProxy);
				final JTextField text = textController.getWidget();
				panel.add(text, new GridBagConstraints(1, - 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			} else if (wBuilder.getNavigator().isTypeNavigatable(type)) {
				final LinkController linkController = wBuilder.caption(label).link().bind(scalarProxy);
				final JLabel link = linkController.getWidget();
				panel.add(link, new GridBagConstraints(1, - 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			} else {
			  final LabelController<JLabel> labelController = wBuilder.caption(label).label().bind(scalarProxy);
              final JLabel valueLabel = labelController.getWidget();
              panel.add(valueLabel, new GridBagConstraints(1, - 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			}
		}

		// collection plugs
		for (ListProperty listProperty : modelInspector.getListProperties(getModelType())) {
			final JLabel label = new JLabel();
			panel.add(label, new GridBagConstraints(0, - 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

			final ListDataProxy listProxy = new ListDataProxy(listProperty, listProperty.getName(), listProperty);
			getMediator().addModelSocket(listProxy);
			
			final TableControllerFactory tableCtrlFactory = wBuilder.caption(label).table();
			final TableController tableController = tableCtrlFactory.bind(listProxy);
			final JTable table = tableController.getWidget();
			final JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setPreferredSize(new Dimension(100, 100));
			panel.add(scrollPane, new GridBagConstraints(1, - 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		}

		return panel;
	}
}
