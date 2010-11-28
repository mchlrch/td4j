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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.IndividualDataContainer;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.binding.model.IndividualDataRelay;
import org.td4j.core.binding.model.ListDataContainer;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.internal.reflect.AbstractExecutable;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.swing.binding.SelectionController;
import org.td4j.swing.binding.TableController;
import org.td4j.swing.binding.WidgetBuilder;
import org.td4j.swing.internal.binding.TableModelAdapter;
import org.td4j.swing.internal.binding.TableSelectionWidgetAdapter;
import org.td4j.swing.workbench.Editor;
import org.td4j.swing.workbench.Form;
import org.td4j.swing.workbench.FormFactory;
import org.td4j.swing.workbench.Workbench;

import ch.miranet.commons.ObjectTK;

public class GenericEditor extends Editor<Object> {
	
	private final Mediator mediator;
	
	private final JLabel typeLabel;
	private final JPanel editor;
	
	private final ListDataContainer listDataContainer;
	private final IndividualDataContainer listSelectionContainer;
	
	private final JSplitPane splitPane;
	private final TableController listTableController;
	private final Form<?> form;

	private static Icon operationsIcon;
	
	private static Icon getOperationsIcon() {
		if (operationsIcon == null) {
			final InputStream iconStream = GenericEditor.class.getResourceAsStream("operations.png");
			final byte[] iconData = new byte[1000];
			try {
				iconStream.read(iconData);
			} catch(Exception ex) {
				ex.printStackTrace(System.err);				
				return null;
			}
			operationsIcon = new ImageIcon(iconData);
		}
		return operationsIcon;
	}
	
	// PEND: muss die connectorFactory in der Signatur sein - jetzt wird WidgetBuilder gebraucht um die Tabelle zu erzeugen

	GenericEditor(Workbench workbench, Class<?> modelType, MetaModel model, FormFactory formFactory) {
		
		// PEND: richtige typsierung einführen nach Typisierung von IModelSocket
		super(workbench, modelType);
		
		this.mediator = new Mediator(modelType);
		ObjectTK.enforceNotNull(model,       "model");
		ObjectTK.enforceNotNull(formFactory, "formFactory");			

		final JPanel header = new JPanel(new GridBagLayout());
		typeLabel = new JLabel(modelType.getName());
		typeLabel.setToolTipText(modelType.getName());
		final Dimension prefSize = typeLabel.getPreferredSize();
		typeLabel.setPreferredSize(new Dimension(0, prefSize.height));
		
		header.add(typeLabel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		final MetaClass metaClass = model.getMetaClass(modelType); 
		final JMenu executableMenu = new JMenu();
		executableMenu.setToolTipText("Operations");
		final Icon opIcon = getOperationsIcon();
		if (opIcon != null) {
			executableMenu.setIcon(opIcon);
		} else {
			executableMenu.setText("Operations");
		}		
		
		for (AbstractExecutable executable : metaClass.getOperations()) {
			executableMenu.add(new InvokeExecutableAction(this, executable));
		}
		if (executableMenu.getItemCount() == 0) executableMenu.setEnabled(false);
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(executableMenu);
		header.add(menuBar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// list table
		listDataContainer = new ListDataContainer(modelType, "listData");
		
		final WidgetBuilder wb = new WidgetBuilder(modelType, model, null);
		
		final NestedPropertiesInEditorListFactory nestedPropsFactory = new NestedPropertiesInEditorListFactory(modelType, model);
		final IndividualProperty[] nestedProperties = nestedPropsFactory.createNestedProperties();
		
		final ListDataProxy listProxy = listDataContainer.createProxy();
		listProxy.setNestedProperties(nestedProperties);
		
		listTableController = wb.table().bind(listProxy);
		final JTable listTable = listTableController.getWidget();
		listTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		
		final JScrollPane listTableScrollPane = new JScrollPane(listTable);
		listTableScrollPane.setPreferredSize(new Dimension(100, 120));
		listTableScrollPane.setMinimumSize(new Dimension(0, 0));
		
		// form
		form = formFactory.createForm(this, modelType);
		final JComponent body = form.getComponent();
		final JPanel bodyContainer = new JPanel(new BorderLayout());
		bodyContainer.add(body, BorderLayout.NORTH);
		final JScrollPane bodyScrollPane = new JScrollPane(bodyContainer);		
		
		// connect table selection to mediator
		listSelectionContainer = new IndividualDataContainer(modelType, "listSelection");
		final IndividualDataProxy listSelectionProxy = listSelectionContainer.createProxy();
		final TableModelAdapter tableModelAdapter = new TableModelAdapter(listTableController.getModel());
		final TableSelectionWidgetAdapter selectionWidget = new TableSelectionWidgetAdapter(listTable);
		final SelectionController listTableSelection = new SelectionController(listTable.getSelectionModel(), tableModelAdapter, listSelectionProxy, selectionWidget);
		final IndividualDataRelay relayMediator = new IndividualDataRelay(listSelectionProxy, mediator);
		final IndividualDataRelay relayForm = new IndividualDataRelay(listSelectionProxy, form);		

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(listTableScrollPane);		
		splitPane.setBottomComponent(bodyScrollPane);		

		editor = new JPanel(new BorderLayout());
		editor.add(header,    BorderLayout.NORTH);
		editor.add(splitPane, BorderLayout.CENTER);			
	}
	
	public Mediator getMediator() {
		return mediator;
	}
		
	public Object getModel() {
		return mediator.getContext();
	}
	
	public Form<?> getForm() {
		return form;
	}
	
	@Override
	public void setContent(EditorContent content) {
		listDataContainer.setContent(new ArrayList<Object>(content.getInstances()));
		listSelectionContainer.setContent(content.getMainObject());
		
	  // force form refresh to update data from models that are not Observable
		form.refreshFromContext();
		
		if (content.getInstances().size() > 1) {
			splitPane.setDividerLocation(-1);
		} else {
			splitPane.setDividerLocation(0);
		}
	}
	
	@Override
	public EditorContent getContent() {
		final EditorContent content = new EditorContent(getContentType(), listDataContainer.getContent(), listSelectionContainer.getContent());
		return content;
	}	

	// PEND: create editor UI lazy
	@Override
	public JComponent getComponent() {
		return editor;
	}
	
	@Override
	public String toString() {
		return "" + getClass().getSimpleName() + "(" + getContentType().getName() + ")";
	}
}