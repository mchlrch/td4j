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

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.ObservableTK;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.workbench.Navigator;



public class LinkController extends ScalarSwingWidgetController<JLabel> {

	private final JLabel widget;

	private final LinkTargetObserver linkTargetObserver = new LinkTargetObserver(this);
	
	public LinkController(JLabel widget, ScalarDataProxy proxy, final Navigator navigator) {
		super(proxy);
		if (widget == null) throw new NullPointerException("widget");
		if (navigator == null) throw new NullPointerException("navigator");

		this.widget = widget;

		widget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				navigator.seek(getDataProxy().readValue());
			}
		});

		setAccess();
		updateView();
	}

	protected void updateView0(Object newTarget) {
		updateLinkTargetChanged(newTarget);
	}
	
	private void updateLinkTargetChanged(Object newTarget) {
		linkTargetObserver.detachFromTarget();
		linkTargetObserver.attachToTarget(newTarget);
		
		updateLinkTargetStateChanged(newTarget);
	}
	
  private void updateLinkTargetStateChanged(Object target) {
  	if (target != null) {
  		final String text = String.format("<html><font color='blue'><u>%s</u></font></html>", target);
  		widget.setText(text);
  		widget.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  	} else {
  		widget.setText("<html><font color='gray'>&lt;null&gt;</font></html>");
  		widget.setCursor(Cursor.getDefaultCursor());
  	}		
	}

	protected Object updateModel0() {
		throw new IllegalStateException();
	}

	@Override
	public JLabel getWidget() {
		return widget;
	}

	@Override
	protected void setAccess() {
		// no write access necessary
	}

	
	// --------------------------------
	private static class LinkTargetObserver implements IObserver {
		
		private final LinkController controller;
		private Object target;
		
		private LinkTargetObserver(LinkController controller) {
			this.controller = ObjectTK.enforceNotNull(controller, "controller");		
		}
		
		private boolean attachToTarget(Object target) {
			if (this.target != null) throw new IllegalStateException("already attached");
			
			this.target = target;
			return ObservableTK.attachObserverToModel(target, this);
		}
		
		private boolean detachFromTarget() {
			final boolean success = ObservableTK.detachObserverFromModel(target, this);
			this.target = null;
			return success;
		}		
	
		@Override
		public void observableChanged(ChangeEvent event) {
			controller.updateLinkTargetStateChanged(target);			
		}
	};
}
