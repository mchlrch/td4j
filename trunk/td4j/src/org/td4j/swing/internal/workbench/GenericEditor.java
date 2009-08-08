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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

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
import org.td4j.core.binding.model.CollectionDataContainer;
import org.td4j.core.binding.model.CollectionDataProxy;
import org.td4j.core.binding.model.IDataConnectorFactory;
import org.td4j.core.binding.model.IScalarDataConnector;
import org.td4j.core.binding.model.ScalarDataContainer;
import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.binding.model.ScalarDataRelay;
import org.td4j.core.internal.reflect.AbstractExecutable;
import org.td4j.core.reflect.ModelInspector;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.binding.SelectionController;
import org.td4j.swing.binding.TableController;
import org.td4j.swing.binding.WidgetBuilder;
import org.td4j.swing.internal.binding.TableModelAdapter;
import org.td4j.swing.workbench.Editor;
import org.td4j.swing.workbench.Form;
import org.td4j.swing.workbench.IFormFactory;
import org.td4j.swing.workbench.Workbench;

public class GenericEditor extends Editor<Object> {
	
	private final Mediator mediator;
	
	private final JLabel typeLabel;
	private final JPanel editor;
	
	private final CollectionDataContainer listDataContainer;
	private final ScalarDataContainer listSelectionContainer;
	
	private final JSplitPane splitPane;
	private final TableController listTableController;
	private final Form<?> form;
	
	// PEND: muss die connectorFactory in der Signatur sein - jetzt wird WidgetBuilder gebraucht um die Tabelle zu erzeugen

	GenericEditor(Workbench workbench, Class<?> modelType, ModelInspector modelInspector, IFormFactory formFactory, IDataConnectorFactory connectorFactory) {
		
		// PEND: richtige typsierung einführen nach Typisierung von IModelSocket
		super(workbench, modelType);
		
		this.mediator = new Mediator(modelType);
		ObjectTK.enforceNotNull(modelInspector,   "modelInspector");
		ObjectTK.enforceNotNull(formFactory,      "formFactory");			
		ObjectTK.enforceNotNull(connectorFactory, "connectorFactory");

		final JPanel header = new JPanel(new GridBagLayout());
		typeLabel = new JLabel(modelType.getName());
		header.add(typeLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,       new Insets(5, 5, 5, 5), 0, 0));
		header.add(new JLabel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		final JMenu executableMenu = new JMenu("executables");
		for (AbstractExecutable executable : modelInspector.getExecutables(modelType)) {
			executableMenu.add(new InvokeExecutableAction(this, executable));
		}
		if (executableMenu.getItemCount() == 0) executableMenu.setEnabled(false);
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(executableMenu);
		header.add(menuBar, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		// list table
		listDataContainer = new CollectionDataContainer(modelType, "listData");
		
		final WidgetBuilder wb = new WidgetBuilder(modelType);
		
		final NestedPropertiesInEditorListFactory nestedPropsFactory = new NestedPropertiesInEditorListFactory(modelType, modelInspector);
		final IScalarDataConnector[] nestedProperties = nestedPropsFactory.createNestedProperties();
		
		final CollectionDataProxy collectionProxy = listDataContainer.createProxy();
		collectionProxy.getConnectorInfo().setNestedProperties(nestedProperties);
		
		listTableController = wb.table().bind(collectionProxy);
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
		listSelectionContainer = new ScalarDataContainer(modelType, "listSelection");
		final ScalarDataProxy listSelectionProxy = listSelectionContainer.createProxy();
		final SelectionController listTableSelection = new SelectionController(listTable.getSelectionModel(), new TableModelAdapter(listTableController.getModel()), listSelectionProxy);
		final ScalarDataRelay relayMediator = new ScalarDataRelay(listSelectionProxy, mediator);
		final ScalarDataRelay relayForm = new ScalarDataRelay(listSelectionProxy, form);		

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
		return mediator.getModel();
	}
	
	public Form<?> getForm() {
		return form;
	}
	
	@Override
	public void setContent(EditorContent content) {
		listDataContainer.setContent(new ArrayList<Object>(content.getInstances()));
		listSelectionContainer.setContent(content.getMainObject());
		
	  // force form refresh to update data from models that are not Observable
		form.refreshFromModel();
		
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