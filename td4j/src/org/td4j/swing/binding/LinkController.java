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

package org.td4j.swing.binding;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.td4j.core.binding.model.ScalarDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.ObservableTK;
import org.td4j.core.tk.ObjectTK;
import org.td4j.swing.workbench.Navigator;



public class LinkController extends ScalarSwingWidgetController<JLabel> {

	private final JLabel widget;
	private final Navigator navigator;
	private final LinkTargetObserver linkTargetObserver = new LinkTargetObserver(this);
	
	public LinkController(final JLabel widget, ScalarDataProxy proxy, final Navigator navigator) {
		super(proxy);
		this.widget = ObjectTK.enforceNotNull(widget, "widget");
		this.navigator = ObjectTK.enforceNotNull(navigator, "navigator");

		widget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			
				// request focus to trigger pending writeModel() operations
				widget.requestFocus();
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						doNavigate();
					}
				});
			}
		});

		setAccess();
		updateView();
	}
	
	protected void doNavigate() {
		final ScalarDataProxy dataProxy = getDataProxy();
		final Class<?> valueType = dataProxy.getValueType();
		final Object value = dataProxy.readValue();
		
		if (URL.class.isAssignableFrom(valueType)) {
			try {
				final URI uri = ((URL)value).toURI();
				Desktop.getDesktop().browse(uri);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			
		} else if (URI.class.isAssignableFrom(valueType)) {
			try {
				final URI uri = (URI)value;
				Desktop.getDesktop().browse(uri);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			
		} else if (File.class.isAssignableFrom(valueType)) {
			try {
				final File file = (File)value;
				Desktop.getDesktop().open(file);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			
		} else {
			navigator.seek(getDataProxy().readValue());
		}
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
