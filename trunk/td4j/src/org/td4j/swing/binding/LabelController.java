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

import java.awt.Component;
import java.lang.reflect.Method;

import org.td4j.core.binding.model.ScalarDataProxy;



// PEND: refactor into common superclass with swt.LabelAdapter
public class LabelController extends ScalarSwingWidgetController<Component> {

	private final Component widget;
	private Method setTextMethod;

	public LabelController(Component widget, ScalarDataProxy proxy) {
		super(proxy);
		if (widget == null) throw new NullPointerException("widget");

		try {
			setTextMethod = widget.getClass().getMethod("setText", new Class[] { String.class });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.widget = widget;

		setAccess();
		updateView();
	}

	protected void updateView0(Object newValue) {
		try {

			// PEND: solve in proxy
			if (newValue != null && ! (newValue instanceof String)) {
				newValue = newValue.toString();
			}

			setTextMethod.invoke(widget, new Object[] { newValue != null ? newValue : "" });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object updateModel0() {
		throw new IllegalStateException("read-only widget");
	}

	@Override
	public Component getWidget() {
		return widget;
	}

	@Override
	protected void setAccess() {
		// PEND:
	}

}
