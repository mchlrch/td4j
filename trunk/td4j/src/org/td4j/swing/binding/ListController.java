/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008 Michael Rauch

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JList;

import org.td4j.core.binding.model.CollectionDataProxy;



public class ListController extends CollectionSwingWidgetController<JList> {

	private final JList widget;
	private final MyListModel model;

	public ListController(JList widget, CollectionDataProxy proxy) {
		super(proxy);
		if (widget == null) throw new NullPointerException("widget");

		this.widget = widget;
		this.model = new MyListModel();
		widget.setModel(model);

		setAccess();
		updateView();
	}

	protected void updateView0(Collection<?> newValue) {
		model.setCollection(newValue);
	}

	@Override
	public JList getWidget() {
		return widget;
	}

	@Override
	protected void setAccess() {
		widget.setEnabled(canRead());
	}


	// --------------------------------
	static class MyListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;

		private List<Object> list;

		private MyListModel() {}
		
		public Object getElementAt(int index) {
			return list.get(index);
		}

		private void setCollection(Collection<?> newValue) {
			list = newValue != null ? new ArrayList<Object>(newValue) : null;

			// PEND: only fire for range that actually changed
			fireContentsChanged(this, 0, list != null ? list.size() - 1 : 0);
		}

		public int getSize() {
			return list != null ? list.size() : 0;
		}

	}

}