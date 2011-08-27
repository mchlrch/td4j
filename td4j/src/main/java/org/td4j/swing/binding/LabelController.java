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

import java.awt.Component;
import java.lang.reflect.Method;

import org.td4j.core.binding.model.IndividualDataProxy;

import ch.miranet.commons.TK;


public class LabelController<T extends Component> extends IndividualSwingWidgetController<T> {

	private final T widget;
	private Method setTextMethod;

	public LabelController(T widget, IndividualDataProxy proxy) {
		super(proxy);
		this.widget = TK.Objects.assertNotNull(widget, "widget");

		try {
			setTextMethod = widget.getClass().getMethod("setText", new Class[] { String.class });
			if (setTextMethod != null && ! setTextMethod.isAccessible()) setTextMethod.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		setAccess();
		updateView();
	}
	
	public T getWidget() {
		return widget;
	}
	
	protected void setAccess() {
		// no action
	}

	protected void updateView0(Object newValue) {
		try {
			if (newValue != null && ! (newValue instanceof String)) {
				newValue = newValue.toString();
			}

			setTextMethod.invoke(widget, new Object[] { newValue != null ? newValue : "" });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object readView0() {
		throw new IllegalStateException("read-only widget");
	}

}
