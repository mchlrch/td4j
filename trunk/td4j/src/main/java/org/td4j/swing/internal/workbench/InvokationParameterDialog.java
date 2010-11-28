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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.internal.binding.model.converter.DefaultConverterRepository;
import org.td4j.core.internal.binding.model.converter.IConverterRepository;
import org.td4j.core.internal.reflect.InvokationParameter;
import org.td4j.swing.binding.WidgetBuilder;

import ch.miranet.commons.ListTK;
import ch.miranet.commons.ObjectTK;


public class InvokationParameterDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();

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
			
			// write widget value to model
			KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();			

			optionType = JOptionPane.OK_OPTION;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setVisible0(false);					
				};
			});
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

		wBuilder.getMediator().setContext(this);

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
		final IConverterRepository repo = DefaultConverterRepository.INSTANCE;
		for (InvokationParameter param : parameterList) {
			paramValueMap.put(param, repo.getNullEquivalentFor(param.getType()));
		}
		wBuilder.getMediator().refreshFromContext();
	}

	private void initUI() {
		setModal(true);
		final Container contentPane = getContentPane();

		contentPane.setLayout(new GridLayout());
		
		final GenericPanelBuilder pBuilder = new GenericPanelBuilder(wBuilder) {
			protected void postAddWidget(Component comp) {
				super.postAddWidget(comp);
				if (getFocusRequester() == null) {
					final FocusRequester focusRequester = new FocusRequester(comp);
					addHierarchyListener(focusRequester);
					setFocusRequester(focusRequester);
				}
			}
		};
		
		for (InvokationParameter param : parameterList) {
			final IndividualDataConnector connector = connectorFactory.createIndividualMethodConnector(getClass(), "parameterValue", new Class[] { InvokationParameter.class }, new Object[] { param });
			
			final String labelTooltip = String.format("%1$s : %2$s", param.getType().getSimpleName(), param.getName());
			pBuilder.addIndividualDataConnector(connector, param.getName(), param.getType(), labelTooltip);
		}

		final JPanel buttons = new JPanel(new GridLayout(1, 3));
		final JButton okButton = new JButton(okAction);
		getRootPane().setDefaultButton(okButton);
		
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
		getRootPane().getActionMap().put("cancel", cancelAction);

		buttons.add(new JButton(clearValuesAction));
		buttons.add(okButton);
		buttons.add(new JButton(cancelAction));

		pBuilder.getPanel().add(buttons, new GridBagConstraints(0, - 1, 2, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(15, 0, 15, 5), 0, 0));

		contentPane.add(pBuilder.getPanel(), BorderLayout.CENTER);
		pack();
	}
	
	
	private FocusRequester focusRequester;
	private FocusRequester getFocusRequester()               { return focusRequester; }
	private void setFocusRequester(FocusRequester requester) { this.focusRequester = requester; }
	

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
	
	
	
	private static class FocusRequester implements HierarchyListener {
		
		private final Component comp;
		
		private FocusRequester(Component comp) {
			this.comp = ObjectTK.enforceNotNull(comp, "comp");
		}		
		
		@Override
		public void hierarchyChanged(HierarchyEvent e) {
			final boolean showingChanged = (e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED;
			final boolean compVisible = e.getComponent().isVisible();
			
			if (showingChanged && compVisible) {
				comp.requestFocusInWindow();				
			}			
		}
	}

}
