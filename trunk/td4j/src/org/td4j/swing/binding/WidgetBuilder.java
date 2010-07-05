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

package org.td4j.swing.binding;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.Caption;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.internal.metamodel.JavaMetaModel;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.core.reflect.DataConnector;
import org.td4j.core.tk.IFilter;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.env.SvcRepository;
import org.td4j.core.tk.filter.AcceptAllFilter;
import org.td4j.swing.internal.binding.ButtonControllerFactory;
import org.td4j.swing.internal.binding.LabelControllerFactory;
import org.td4j.swing.internal.binding.LinkControllerFactory;
import org.td4j.swing.internal.binding.ListControllerFactory;
import org.td4j.swing.internal.binding.ListModelAdapter;
import org.td4j.swing.internal.binding.SelectionControllerFactory;
import org.td4j.swing.internal.binding.TableControllerFactory;
import org.td4j.swing.internal.binding.TableModelAdapter;
import org.td4j.swing.internal.binding.TextControllerFactory;
import org.td4j.swing.workbench.Navigator;



// stateful builder for widgets
// PEND: refactor common superclass swt.WidgetBuilder
public class WidgetBuilder<T> {

	private final Mediator mediator;
	private final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();
	private final MetaModel metaModel;
	private final Navigator navigator;

	private boolean autoCaptions = true;
	private Caption currentCaption;

	public WidgetBuilder(Class<?> observableType) {
		this(observableType, null);
	}

	public WidgetBuilder(Class<?> observableType, Navigator navigator) {
		this(new Mediator(observableType), navigator);
	}

	public WidgetBuilder(Mediator mediator, Navigator navigator) {
		this(mediator, navigator, new JavaMetaModel(new SvcRepository()));
	}
	
	public WidgetBuilder(Mediator mediator, Navigator navigator, MetaModel metaModel) {
		this.mediator = ObjectTK.enforceNotNull(mediator, "mediator");
		this.metaModel = ObjectTK.enforceNotNull(metaModel, "metaModel");

		this.navigator = navigator;
	}

	public Mediator getMediator() {
		return mediator;
	}

	@SuppressWarnings("unchecked")
	public T getContext() {
		return (T) mediator.getContext();
	}

	public void setContext(T ctx) {
		mediator.setContext(ctx);
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public boolean isAutoCaptions() {
		return autoCaptions;
	}

	public void setAutoCaptions(boolean autoCaptions) {
		this.autoCaptions = autoCaptions;
	}

	// ========================================
	// ==== Text ==============================
	public TextControllerFactory text() {
		widgetPreCreate();
		final JTextField widget = new JTextField();
		
		final FocusAdapter selectTextOnFocusGained = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				widget.selectAll();				
			}
		};
		widget.addFocusListener(selectTextOnFocusGained);
		
		return text(widget);
	}

	public TextControllerFactory text(JTextField widget) {
		final TextControllerFactory factory = new TextControllerFactory(mediator, connectorFactory, widget, useCurrentCaption());
		return factory;
	}

	// ========================================
	// ==== Link ==============================

	public LinkControllerFactory link() {
		widgetPreCreate();
		final JLabel widget = new JLabel();
		adjustLabelFont(widget);
		return link(widget);
	}

	public LinkControllerFactory link(JLabel widget) {
		final LinkControllerFactory factory = new LinkControllerFactory(mediator, connectorFactory, widget, useCurrentCaption(), navigator);
		return factory;
	}

	// ========================================
	// ==== Button ============================
	public ButtonControllerFactory button() {
		widgetPreCreate();
		final JCheckBox widget = new JCheckBox();
		return button(widget);
	}

	public ButtonControllerFactory button(AbstractButton widget) {
		final ButtonControllerFactory factory = new ButtonControllerFactory(mediator, connectorFactory, widget, useCurrentCaption());
		return factory;
	}

	// PEND: what about mnemonic focus to widget?
	// ======================================
	// ======== Captions for Widgets ========
	public WidgetBuilder<T> caption() {
		final JLabel label = new JLabel();
		return caption(label);
	}

	public WidgetBuilder<T> caption(JLabel widget) {
		if (currentCaption != null) throw new IllegalStateException("caption pending");
		this.currentCaption = new LabelCaption(widget);
		return this;
	}

	public WidgetBuilder<T> caption(Caption widget) {
		if (currentCaption != null) throw new IllegalStateException("caption pending");
		this.currentCaption = ObjectTK.enforceNotNull(widget, "widget");
		return this;
	}

	// ========================================
	// ==== Label ============================
	public LabelControllerFactory<JLabel> label() {
		widgetPreCreate();
		final JLabel widget = new JLabel();
		adjustLabelFont(widget);
		return label(widget);
	}

	public <L extends Component> LabelControllerFactory<L> label(L widget) {
		final LabelControllerFactory<L> factory = new LabelControllerFactory<L>(mediator, connectorFactory, widget, useCurrentCaption());
		return factory;
	}

	// ========================================
	// ==== List ==============================
	public ListControllerFactory list() {
		widgetPreCreate();
		final JList widget = new JList();
		return list(widget);
	}

	public ListControllerFactory list(JList widget) {
		final ListControllerFactory factory = new ListControllerFactory(mediator, connectorFactory, widget, useCurrentCaption());
		return factory;
	}

	public SelectionControllerFactory selection(JList list) {
		final ListModelAdapter modelAdapter = new ListModelAdapter(list.getModel());
		final SelectionControllerFactory factory = new SelectionControllerFactory(mediator, connectorFactory, list.getSelectionModel(), modelAdapter);
		return factory;
	}
	
	
	// ========================================
	// ==== Table =============================
	private static final IFilter<DataConnector> acceptAllColumnsFilter = AcceptAllFilter.getInstance();

	public TableControllerFactory table() {
		widgetPreCreate();
		final JTable widget = new JTable();
		widget.setRowHeight(22);
		
		final TableCellRenderer headerRenderer = widget.getTableHeader().getDefaultRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer) {
			((DefaultTableCellRenderer)headerRenderer).setHorizontalAlignment(JLabel.LEFT);
		}
        
		return table(widget);
	}
	
	public TableControllerFactory table(JTable widget) {
	  final TableControllerFactory factory = new TableControllerFactory(mediator, connectorFactory, metaModel, widget, useCurrentCaption(), navigator);
	  return factory;
	}	

	public SelectionControllerFactory selection(JTable table) {
		final TableModel tableModel = table.getModel();
		if ( ! (tableModel instanceof TableController.MyTableModel)) {
			throw new IllegalStateException("tableModel must be " + TableController.MyTableModel.class.getName());
		}
		
		final TableModelAdapter modelAdapter = new TableModelAdapter( (TableController.MyTableModel) tableModel );		
		final SelectionControllerFactory factory = new SelectionControllerFactory(mediator, connectorFactory, table.getSelectionModel(), modelAdapter);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return factory;
	}
	
	
	// ========================================
	protected void widgetPreCreate() {
		if (autoCaptions && currentCaption == null) {
			caption();
		}
	}

	private Caption useCurrentCaption() {
		final Caption result = currentCaption;
		currentCaption = null;
		return result;
	}
	
	private static Font plainLabelFont;
	protected JLabel adjustLabelFont(JLabel label) {
	  if (plainLabelFont == null) plainLabelFont = label.getFont().deriveFont(Font.PLAIN);
	  
	  label.setFont(plainLabelFont);
	  return label;
	}

}
