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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.ObservableTK;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.tk.ArrayTK;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.workbench.Navigator;
import org.td4j.swing.workbench.Editor.EditorContent;


public class TableController extends ListSwingWidgetController<JTable> {
  
	private final JTable table;
	private final MyTableModel model;
	
	public TableController(final JTable table, final ListDataProxy proxy, final Navigator navigator) {
		super(proxy);
		this.table = ObjectTK.enforceNotNull(table, "table");
		
		proxy.ensureSensibleNestedProperties(null);
		this.model = new MyTableModel(proxy, proxy.getNestedProperties());

		table.setModel(model);

		if (navigator != null) {
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() % 2 != 0) return;

					final int rowIndex = table.rowAtPoint(e.getPoint());
					if (rowIndex >= 0) {						
						final Object rowObject = model.getRowAt(rowIndex);
						final List<Object> allRows = model.getRows();
						final EditorContent content = new EditorContent(proxy.getValueType(), allRows, rowObject);
						navigator.seek(content);
					}
				}
			});
		}
		
		setAccess();
		updateView();
	}

	protected void updateView0(List<?> newValue) {
		model.setContent(newValue);
	}

	@Override
	public JTable getWidget() {
		return table;
	}

	public MyTableModel getModel() {
		return model;
	}

	@Override
	protected void setAccess() {
		table.setEnabled(canRead());
	}


	// --------------------------------
	public static class MyTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		private final Class<?> rowType;
		private final IndividualProperty[] columnProperties;
		private final List<Object> rowObjects = new ArrayList<Object>();

		private final RowObserver rowObserver = new RowObserver(this);

		private MyTableModel(final ListDataProxy proxy, IndividualProperty[] columnProperties) {			
			this.rowType = ObjectTK.enforceNotNull(proxy.getValueType(), "proxy.getType()");
			this.columnProperties = ArrayTK.enforceNotEmpty(columnProperties, "columnProperties");
		}

		@Override
		public int getColumnCount() {
			return columnProperties.length;
		}

		@Override
		public int getRowCount() {
			return rowObjects.size();
		}

		public List<Object> getRows() {
			return new ArrayList<Object>(rowObjects);
		}
		
		public Object getRowAt(int rowIndex) {
			return rowObjects.get(rowIndex);
		}

		@Override
	    public String getColumnName(int columnIndex) {
		    final IndividualProperty prop = columnProperties[columnIndex];
		    return prop.getName();
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			final IndividualProperty prop = columnProperties[columnIndex];
			final Class<?> valueType = prop.getValueType();
			
			// TODO _this is a workaround to make primitive types work 
			if (int.class == valueType) return Integer.class;
			else if (long.class == valueType) return Long.class;
			else if (float.class == valueType) return Float.class;
			else if (double.class == valueType) return Double.class;
			else if (boolean.class == valueType) return Boolean.class;
			
		  return valueType;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			final IndividualProperty prop = columnProperties[columnIndex];
			final Object value = prop.readValue(rowObjects.get(rowIndex));
			return value;
		}

		private void setContent(List<?> newValue) {
			rowObjects.clear();

			if (newValue != null) {
				rowObjects.addAll(newValue);
			}

			rowObserver.attachToTargets(rowObjects);

			fireTableDataChanged();
		}
	}


	// --------------------------------
	private static class RowObserver implements IObserver {

		private final AbstractTableModel tableModel;
		private final Set<Object> observables = new HashSet<Object>();

		private RowObserver(AbstractTableModel tableModel) {
			this.tableModel = ObjectTK.enforceNotNull(tableModel, "tableModel");
		}

		private void attachToTargets(List<?> targets) {
			final Set<Object> newTargets = new HashSet<Object>(targets);

			// detach from obsolete targets
			for (Iterator<Object> it = observables.iterator(); it.hasNext();) {
				final Object observable = it.next();
				if ( ! newTargets.contains(observable)) {
					ObservableTK.detachObserverFromModel(observable, this);
					it.remove();
				}
			}

			// attach to new targets
			for (Object observable : newTargets) {
				if ( ! observables.contains(observable)) {
					ObservableTK.attachObserverToModel(observable, this);
					observables.add(observable);
				}
			}
		}

		@Override
		public void observableChanged(ChangeEvent event) {

			// PEND: fire more precisely
			tableModel.fireTableDataChanged();
		}
	};

}
