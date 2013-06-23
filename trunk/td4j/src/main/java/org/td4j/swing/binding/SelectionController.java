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

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;
import org.td4j.swing.binding.TableController.AroundTableChangedListener;
import org.td4j.swing.internal.binding.ListModelAdapter;
import org.td4j.swing.internal.binding.ListSelectionWidgetAdapter;
import org.td4j.swing.internal.binding.TableModelAdapter;
import org.td4j.swing.internal.binding.TableSelectionWidgetAdapter;

import ch.miranet.commons.TK;

public class SelectionController implements ListSelectionListener, IObserver {

	protected final ListSelectionModel selectionModel;
	protected final OrderedElementModel dataModel;
	protected final SelectionWidget selectionWidget;
	protected final IndividualDataProxy proxy;

	private boolean proxyToSelectionSyncInProgress;
	private boolean fireTableChangedInProgress;

	public static SelectionController createSelectionController(JList<?> list, IndividualDataProxy proxy) {
		final ListModelAdapter listModelAdapter = new ListModelAdapter(list.getModel());
		final ListSelectionWidgetAdapter selectionWidget = new ListSelectionWidgetAdapter(list);

		return new SelectionController(list.getSelectionModel(), listModelAdapter, proxy, selectionWidget);
	}	
	
	public static SelectionController createSelectionController(TableController tableController, IndividualDataProxy proxy) {
		final JTable table = tableController.getWidget();
		
		final TableModelAdapter tableModelAdapter = new TableModelAdapter(tableController.getModel());
		final TableSelectionWidgetAdapter selectionWidget = new TableSelectionWidgetAdapter(table);
		
		final SelectionController result = new SelectionController(table.getSelectionModel(), tableModelAdapter, proxy, selectionWidget);
		
		final AroundTableChangedListener selectionKeeper = new AroundTableChangedListener() {
			
			@Override
			public void beforeFireTableChange() {
				result.fireTableChangedInProgress = true;				
			}
			
			@Override
			public void afterFireTableChange() {
				result.fireTableChangedInProgress = false;
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						result.updateSelectedElementFromProxy();						
					}
				});				
			}
		};
		tableController.getModel().addAroundTableChangedListener(selectionKeeper);

		return result;
	}
	
	
	public SelectionController(ListSelectionModel selectionModel, OrderedElementModel dataModel, IndividualDataProxy proxy, SelectionWidget selectionWidget) {
		this.dataModel = TK.Objects.assertNotNull(dataModel, "dataModel");
		this.proxy = TK.Objects.assertNotNull(proxy, "proxy");
		this.selectionWidget = TK.Objects.assertNotNull(selectionWidget, "selectionWidget");

		this.selectionModel = selectionModel;

		selectionModel.addListSelectionListener(this);
		proxy.addObserver(this);
	}

	public IndividualDataProxy getDataProxy() {
		return proxy;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		if (proxyToSelectionSyncInProgress || fireTableChangedInProgress)
			return;
		if (!canWrite())
			return;

		final Object selection = this.findSelectedElement(e);
		this.writeSelectedElementToProxy(selection);
	}
	
	protected Object findSelectedElement(ListSelectionEvent e) {
		final int index = selectionModel.getMinSelectionIndex();
		if (index >= 0 && index < dataModel.getSize()) {
			return dataModel.getElementAt(index);
		} else {
			return null;
		}		
	}
	
	protected void writeSelectedElementToProxy(Object selection) {
		proxy.writeValue(selection);
	}

	@Override
	public void observableChanged(ChangeEvent event) {
		if (event.getType() != ChangeEvent.Type.StateChange) {
			return;
			
		} else {
			this.updateSelectedElementFromProxy();
		}
	}

	protected void updateSelectedElementFromProxy() {
		selectionWidget.setSelectionEnabled(canWrite());

		proxyToSelectionSyncInProgress = true;
		try {
			final Object selectionFromProxy = canRead() ? proxy.readValue() : null;
			if (selectionFromProxy == null) {
				selectionModel.clearSelection();

				// lookup object in listModel
			} else {
				final int index = indexOfObject(selectionFromProxy);
				if (index >= 0) {
					selectionModel.setSelectionInterval(index, index);
					
				} else if (canWrite()) {
					// triggers another call to updateSelectedElementFromProxy() that will do selectionModel.clearSelection() 
					proxy.writeValue(null); 
					
				} else {
					selectionModel.clearSelection();
				}
			}
		} finally {
			proxyToSelectionSyncInProgress = false;
		}
	}

	protected boolean isProxyToSelectionSyncInProgress() {
		return proxyToSelectionSyncInProgress;
	}
	
	protected boolean isFireTableChangedInProgress() {
		return fireTableChangedInProgress;
	}

	private int indexOfObject(Object obj) {
		TK.Objects.assertNotNull(obj, "obj");

		for (int i = 0, n = dataModel.getSize(); i < n; i++) {
			final Object candidate = dataModel.getElementAt(i);
			if (obj.equals(candidate))
				return i;
		}
		return -1;
	}

	protected boolean canWrite() {
		return proxy != null && proxy.canWrite();
	}

	protected boolean canRead() {
		return proxy != null && proxy.canRead();
	}
	
}
