/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.td4j.core.binding.model.DefaultDataConnectorFactory;
import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.internal.binding.model.converter.DefaultConverterRepository;
import org.td4j.core.internal.binding.model.converter.IConverter;
import org.td4j.core.internal.reflect.InvokationParameter;
import org.td4j.core.tk.ListTK;
import org.td4j.swing.binding.ButtonController;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.binding.WidgetBuilder;


public class InvokationParameterDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final DefaultDataConnectorFactory connectorFactory = new DefaultDataConnectorFactory();

	private final List<InvokationParameter> parameterList;
	private final HashMap<InvokationParameter, Object> paramValueMap = new HashMap<InvokationParameter, Object>();
	private final WidgetBuilder<InvokationParameterDialog> wBuilder = new WidgetBuilder<InvokationParameterDialog>(InvokationParameterDialog.class);

	private int optionType;

	private final Action clearValuesAction = new AbstractAction("Clear") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(java.awt.event.ActionEvent e) {
			clearParamValueMap();
		};
	};

	private final Action okAction = new AbstractAction("OK") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(java.awt.event.ActionEvent e) {
			optionType = JOptionPane.OK_OPTION;
			setVisible0(false);
		};
	};

	private final Action cancelAction = new AbstractAction("Cancel") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(java.awt.event.ActionEvent e) {
			optionType = JOptionPane.CANCEL_OPTION;
			setVisible0(false);
		};
	};

	public InvokationParameterDialog(List<InvokationParameter> params) {
		ListTK.enforceNotEmpty(params, "params");
		this.parameterList = new ArrayList<InvokationParameter>(params);

		wBuilder.getMediator().setModel(this);

		clearParamValueMap(); // init to null values

		initUI();
	}

	@Override
	public void setVisible(boolean b) {
		optionType = JOptionPane.CANCEL_OPTION;
		setVisible0(b);
	}

	private void setVisible0(boolean b) {
		super.setVisible(b);
	}

	private void clearParamValueMap() {
		for (InvokationParameter param : parameterList) {
			paramValueMap.put(param, null);
		}
		wBuilder.getMediator().refreshFromModel();
	}

	private void initUI() {
		setModal(true);
		final Container contentPane = getContentPane();

		contentPane.setLayout(new GridBagLayout());
		for (InvokationParameter param : parameterList) {
			final IScalarDataConnector connector = connectorFactory.createScalarMethodConnector(getClass(), "parameterValue", new Class[] { InvokationParameter.class }, new Object[] { param });
			
            // PEND: fix this, temporary only conversion to String supported !!
            final Class<?> fromType = param.getType();
            final Class<?> toType = String.class;
            final IConverter converter = DefaultConverterRepository.INSTANCE.getConverter(fromType, toType);
			final ScalarDataProxy dataProxy = connector.createProxy(converter);
			wBuilder.getMediator().addModelSocket(dataProxy);
			
			final JLabel caption = new JLabel(param.getName());
			caption.setToolTipText(String.format("%1$s : %2$s", param.getType().getSimpleName(), param.getName()));
			contentPane.add(caption, new GridBagConstraints(0, - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));

			// PEND: blend with code from GenericForm
			if (param.getType() == Boolean.class) {
				final ButtonController btnController = wBuilder.button().bindConnector(connector);
				final AbstractButton btn = btnController.getWidget();
				contentPane.add(btn, new GridBagConstraints(1, - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
			} else {

				// PEND: combobox

				// if (param.getType() == String.class) {
				final TextController textController = wBuilder.text().bind(dataProxy);
				final JTextField text = textController.getWidget();
				contentPane.add(text, new GridBagConstraints(1, - 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
				// } else {
				// final LinkController linkController =
				// wBuilder.link().bindConnector(connector);
				// final JLabel link = linkController.getWidget();
				// contentPane.add(link, new GridBagConstraints(1, - 1, 1, 1, 1.0, 0.0,
				// GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0,
				// 5), 0, 0));
			}
		}

		final JPanel buttons = new JPanel(new GridLayout(1, 3));
		final JButton okButton = new JButton(okAction);
		getRootPane().setDefaultButton(okButton);

		buttons.add(new JButton(clearValuesAction));
		buttons.add(okButton);
		buttons.add(new JButton(cancelAction));

		contentPane.add(buttons, new GridBagConstraints(0, - 1, 2, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(15, 0, 15, 5), 0, 0));

		pack();
	}

	void setParameterValue(InvokationParameter param, Object value) {
		paramValueMap.put(param, value);
	}

	Object getParameterValue(InvokationParameter param) {
		return paramValueMap.get(param);
	}

	public int getOptionType() {
		return optionType;
	}

	public Object[] getParameterValues() {
		final List<Object> paramValues = new ArrayList<Object>(parameterList.size());
		for (InvokationParameter param : parameterList) {
			paramValues.add(paramValueMap.get(param));
		}
		return paramValues.toArray();
	}

}
