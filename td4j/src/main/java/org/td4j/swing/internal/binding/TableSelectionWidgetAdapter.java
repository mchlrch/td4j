/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2010 Michael Rauch

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

package org.td4j.swing.internal.binding;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.td4j.swing.binding.SelectionWidget;

import ch.miranet.commons.ObjectTK;

public class TableSelectionWidgetAdapter implements SelectionWidget {
	
	private final JTable table;
	private Boolean prevState;
	
	public TableSelectionWidgetAdapter(JTable table) {
		this.table = ObjectTK.enforceNotNull(table, "table");
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	@Override
	public void setSelectionEnabled(boolean enabled) {
		if (prevState != null && prevState == enabled) return;
		 
		if ( ! enabled) {
			
			// PEND: implement this
			throw new IllegalStateException("not implemented.");
			
		// add mouse listeners
		} else {
			
		// PEND: implement this
		}		
	}

}
