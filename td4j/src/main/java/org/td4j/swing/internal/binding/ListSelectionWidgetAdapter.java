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

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.td4j.swing.binding.SelectionWidget;

import ch.miranet.commons.TK;

public class ListSelectionWidgetAdapter implements SelectionWidget {
	
	private final JList list;
	private Boolean prevState;
	
	// listeners are removed when selection is disabled. they are kept by reference to be able to enable selection again afterwards
	private final List<MouseListener> mouseListeners = new ArrayList<MouseListener>();
	private final List<MouseMotionListener> mouseMotionListeners = new ArrayList<MouseMotionListener>();
	
	public ListSelectionWidgetAdapter(JList list) {
		this.list = TK.Objects.assertNotNull(list, "list");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	@Override
	public void setSelectionEnabled(boolean enabled) {
		if (prevState != null && prevState == enabled) return;
		
		// remove mouse listeners, but keep them 
		if ( ! enabled) {
			for (MouseListener ml : list.getMouseListeners()) {
				list.removeMouseListener(ml);
				mouseListeners.add(ml);
			}
			for (MouseMotionListener ml : list.getMouseMotionListeners()) {
				list.removeMouseMotionListener(ml);
				mouseMotionListeners.add(ml);
			}
			
			// PEND: 'disabled' cursor anzeigen ?!
			
		// add mouse listeners
		} else {
			for (MouseListener ml : mouseListeners) {
				list.addMouseListener(ml);
			}
			for (MouseMotionListener ml : mouseMotionListeners) {
				list.addMouseMotionListener(ml);
			}
			mouseListeners.clear();
			mouseMotionListeners.clear();
		}		
	}

}
