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

package org.td4j.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;

import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.binding.model.ListDataConnector;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeSupport;
import org.td4j.core.model.IObservable;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.Observable;
import org.td4j.examples.order.Person;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.TextController;

import ch.miranet.commons.TK;



public class ChoiceUISwing extends JPanel {
	private static final long serialVersionUID = 1L;

	ChoiceUISwing() {
		setLayout(new GridBagLayout());

		final JLabel addressLabel = new JLabel("address");
		final JTextField addressText = new JTextField("-address-");

		final Insets insets = new Insets(5, 5, 5, 5);
		add(addressLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		add(addressText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));

		final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();
		
		// direct mit proxy
		final IndividualDataConnector addressFieldPlug = connectorFactory.createIndividualFieldConnector(Person.class, "address");
		final IndividualDataProxy addressFieldProxy = new IndividualDataProxy(addressFieldPlug, "address");
		final TextController adrTextController = new TextController(addressText, addressFieldProxy, false);
		addressFieldProxy.setContext(Person.BART);

		// list: direct mit proxy
		final JList addressList = new JList();
		final ListDataConnector addressListFieldPlug = connectorFactory.createListFieldConnector(Person.class, "addressChoice");

		final MyFilter myFilter = new MyFilter();
		final FilteredListDataConnector addressFilteredPlug = new FilteredListDataConnector(addressListFieldPlug, myFilter);

		// PEND: mit IUpdateHandler interface wäre schöneres proxy machen möglich
		// final UpdateHandler origUpdateHandler =
		// ValueProxy.createUpdateHandler(addressFilteredPlug, "addressChoice");

		final ChangeSupport changeSupport = new ChangeSupport(this);

		final IObserver myUpdateHandler = new IObserver() {
			public void observableChanged(ChangeEvent event) {
				changeSupport.fireStateChange();
			}
		};
		myFilter.addObserver(myUpdateHandler);
		final ListDataProxy addressListFieldProxy = new ListDataProxy(addressFilteredPlug, "foo");

		addressText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				myFilter.setFilterText(addressText.getText());
			}
		});

		PopupFactory factory = PopupFactory.getSharedInstance();
		JLabel popupLabel = new JLabel("pop-up label");
		final Popup popup = factory.getPopup(addressText, popupLabel, 0, 0);

		addressText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				popup.show();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});

		addressList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addressList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					// PEND: selectionModel wird nicht richtig nachgeführt wenn
					// collectionValue ändert
					// drumm prüfen wir hier noch selber von hand

					// Object val = addressList.getSelectedValue();

					final int index = addressList.getSelectedIndex();
					final ListModel listModel = addressList.getModel();
					if (index >= 0 && index < listModel.getSize()) {
						final Object selectedObject = listModel.getElementAt(index);
						System.out.println(selectedObject);
					}

				}
			}
		});
		// PEND: selection auch mit Doppelclick ausführbar

		new ListController(addressList, addressListFieldProxy);
		addressListFieldProxy.setContext(Person.BART);

		add(addressList, new GridBagConstraints(1, - 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, insets, 0, 0));
	}


	private class MyFilter extends Observable implements IPlugFilter {
		private String filterText;

		public boolean accept(Object o) {
			if (o == null) return false;
			if (filterText == null) return true;

			final String oAsString = o.toString();
			return oAsString != null && oAsString.indexOf(filterText) >= 0;
		}

		void setFilterText(String s) {
			this.filterText = s;
			changeSupport.fireStateChange();
		}
	}


	// PEND: UpdateHandler muss auch auf Filter stateChange() reagieren
	public static interface IPlugFilter extends IObservable {
		public boolean accept(Object o);
	}


	private static class FilteredListDataConnector implements ListDataConnector {
		private final ListDataConnector delegate;
		private final IPlugFilter filter;

		FilteredListDataConnector(ListDataConnector delegate, IPlugFilter filter) {
			this.delegate = TK.Objects.assertNotNull(delegate, "delegate");
			this.filter = filter;
		}

		// delegate methods

		public Class<?> getContextType()     { return delegate.getContextType(); }
		public Class<?> getValueType()       { return delegate.getValueType(); }
		public boolean canRead()             { return delegate.canRead(); }		
		public boolean canRead(Object model) { return delegate.canRead(model); }

		public List<?> readValue(Object ctx) {
			List<?> origColl = delegate.readValue(ctx);
			if (origColl == null)
				return null;
			else if (origColl.isEmpty()) return Collections.emptyList();

			List<Object> filteredColl = new ArrayList<Object>(origColl.size());
			for (Object o : origColl) {
				if (filter.accept(o)) filteredColl.add(o);
			}

			return filteredColl;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final ChoiceUISwing panel = new ChoiceUISwing();
				final JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
