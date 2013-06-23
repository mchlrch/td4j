/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008-2013 Michael Rauch

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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.Caption;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.metamodel.MetaClassProvider;
import org.td4j.swing.internal.binding.ButtonControllerFactory;
import org.td4j.swing.internal.binding.LabelControllerFactory;
import org.td4j.swing.internal.binding.LinkControllerFactory;
import org.td4j.swing.internal.binding.ListControllerFactory;
import org.td4j.swing.internal.binding.ListSelectionControllerFactory;
import org.td4j.swing.internal.binding.TableControllerFactory;
import org.td4j.swing.internal.binding.TableSelectionControllerFactory;
import org.td4j.swing.internal.binding.TextControllerFactory;
import org.td4j.swing.workbench.Navigator;

import ch.miranet.commons.TK;
import ch.miranet.commons.container.Option;

// stateful builder for widgets
// PEND: refactor common superclass swt.WidgetBuilder
public class WidgetBuilder<T> {

	private final Mediator<T> mediator;
	private final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();

	private final Option<Navigator> navigator;

	private boolean autoCaptions = true;
	private Caption currentCaption;

	// working with the metaModel is optional, metaModel is only used for
	// nestedProperties in table widget
	private final Option<MetaClassProvider> metaModel;

	public WidgetBuilder(Class<T> observableType) {
		this(observableType, null, null);
	}

	public WidgetBuilder(Mediator<T> mediator) {
		this(mediator, null, null);
	}

	public WidgetBuilder(Class<T> observableType, MetaClassProvider metaModel) {
		this(new Mediator<T>(observableType), metaModel, null);
	}

	public WidgetBuilder(Class<T> observableType, MetaClassProvider metaModel, Navigator navigator) {
		this(new Mediator<T>(observableType), metaModel, navigator);
	}

	public WidgetBuilder(Mediator<T> mediator, MetaClassProvider metaModel) {
		this(mediator, metaModel, null);
	}

	public WidgetBuilder(Mediator<T> mediator, MetaClassProvider metaModel, Navigator navigator) {
		this.mediator = TK.Objects.assertNotNull(mediator, "mediator");
		this.metaModel = new Option<MetaClassProvider>(metaModel);
		this.navigator = new Option<Navigator>(navigator);
	}

	public Mediator<T> getMediator() {
		return mediator;
	}

	public Option<Navigator> getNavigator() {
		return navigator;
	}

	public boolean isAutoCaptions() {
		return autoCaptions;
	}

	public void setAutoCaptions(boolean autoCaptions) {
		this.autoCaptions = autoCaptions;
	}

	public Option<MetaClassProvider> getMetaModel() {
		return metaModel;
	}

	// ========================================
	// ==== Text ==============================
	public TextControllerFactory<JTextField> text() {
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

	public <W extends JTextComponent> TextControllerFactory<W> text(W widget) {
		final TextControllerFactory<W> factory = new TextControllerFactory<W>(mediator, connectorFactory, widget, useCurrentCaption());
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
		if (this.navigator.isNone()) {
			throw new IllegalStateException("WidgetBuilder does not have a navigator, therefore links are not supported.");
		}
		final LinkControllerFactory factory = new LinkControllerFactory(mediator, connectorFactory, widget, useCurrentCaption(), this.navigator.get());
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
		if (currentCaption != null)
			throw new IllegalStateException("caption pending");
		this.currentCaption = new LabelCaption(widget);
		return this;
	}

	public WidgetBuilder<T> caption(AbstractButton widget) {
		if (currentCaption != null)
			throw new IllegalStateException("caption pending");
		this.currentCaption = new ButtonCaption(widget);
		return this;
	}

	public WidgetBuilder<T> caption(Caption widget) {
		if (currentCaption != null)
			throw new IllegalStateException("caption pending");
		this.currentCaption = TK.Objects.assertNotNull(widget, "widget");
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
		final JList<Object> widget = new JList<Object>();
		return list(widget);
	}

	public ListControllerFactory list(JList<?> widget) {
		final ListControllerFactory factory = new ListControllerFactory(mediator, connectorFactory, metaModel, widget, useCurrentCaption());
		return factory;
	}

	public ListSelectionControllerFactory selection(JList<?> list) {
		final ListSelectionControllerFactory factory = new ListSelectionControllerFactory(mediator, connectorFactory, list);
		return factory;
	}

	// ========================================
	// ==== Table =============================

	public TableControllerFactory table() {
		widgetPreCreate();
		final JTable widget = new JTable();
		widget.setRowHeight(22);

		final TableCellRenderer headerRenderer = widget.getTableHeader().getDefaultRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer) {
			((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(JLabel.LEFT);
		}

		return table(widget);
	}

	public TableControllerFactory table(JTable widget) {
		final TableControllerFactory factory = new TableControllerFactory(mediator, connectorFactory, metaModel, widget, useCurrentCaption(), navigator);
		return factory;
	}

	public TableSelectionControllerFactory selection(TableController tableController) {
		final TableSelectionControllerFactory factory = new TableSelectionControllerFactory(mediator, connectorFactory, tableController);
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
		if (plainLabelFont == null)
			plainLabelFont = label.getFont().deriveFont(Font.PLAIN);

		label.setFont(plainLabelFont);
		return label;
	}

}
