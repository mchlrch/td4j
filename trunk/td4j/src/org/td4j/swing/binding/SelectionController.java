/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2010 Michael Rauch

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

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;

import ch.miranet.commons.ObjectTK;



public class SelectionController implements ListSelectionListener, IObserver {

	private final ListSelectionModel selectionModel;
	private final OrderedElementModel dataModel;
	private final SelectionWidget selectionWidget;
	private final IndividualDataProxy proxy;

	private boolean proxyToSelectionSyncInProgress;

	public SelectionController(ListSelectionModel selectionModel, OrderedElementModel dataModel, IndividualDataProxy proxy, SelectionWidget selectionWidget) {
		this.dataModel = ObjectTK.enforceNotNull(dataModel, "dataModel");
		this.proxy = ObjectTK.enforceNotNull(proxy, "proxy");
		this.selectionWidget = ObjectTK.enforceNotNull(selectionWidget, "selectionWidget");

		this.selectionModel = selectionModel;

		selectionModel.addListSelectionListener(this);
		proxy.addObserver(this);
	}

	public IndividualDataProxy getDataProxy() {
		return proxy;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;
		if (proxyToSelectionSyncInProgress) return;
		if ( ! canWrite()) return;

		final int index = selectionModel.getMinSelectionIndex();
		Object selection = null;
		if (index >= 0 && index < dataModel.getSize()) {
			selection = dataModel.getElementAt(index);
		}

		proxy.writeValue(selection);
	}

	@Override
	public void observableChanged(ChangeEvent event) {
		if (event.getType() != ChangeEvent.Type.StateChange) return;

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
				} else {
					selectionModel.clearSelection();
				}
			}
		} finally {
			proxyToSelectionSyncInProgress = false;
		}
	}

	private int indexOfObject(Object obj) {
		ObjectTK.enforceNotNull(obj, "obj");

		for (int i = 0, n = dataModel.getSize(); i < n; i++) {
			final Object candidate = dataModel.getElementAt(i);
			if (obj.equals(candidate)) return i;
		}
		return - 1;
	}
	
	
	private boolean canWrite() {
		return proxy != null && proxy.canWrite();
	}

	private boolean canRead() {
		return proxy != null && proxy.canRead();
	}

}
