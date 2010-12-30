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

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.ObservableTK;
import org.td4j.swing.workbench.Navigator;

import ch.miranet.commons.ObjectTK;



public class LinkController extends IndividualSwingWidgetController<JLabel> {

	private static final LinkHandler linkHandler = new LinkHandler();
	
	private final JLabel widget;
	private final Navigator navigator;
	private final LinkTargetObserver linkTargetObserver = new LinkTargetObserver(this);
	
	public LinkController(final JLabel widget, IndividualDataProxy proxy, final Navigator navigator) {
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
		final IndividualDataProxy dataProxy = getDataProxy();
		final Class<?> valueType = dataProxy.getValueType();
		final Object value = dataProxy.canRead() ? dataProxy.readValue() : null;
		
		if (value == null) return;
		
		if (linkHandler.canBrowse() && URL.class.isAssignableFrom(valueType)) {
			try {
				final URI uri = ((URL)value).toURI();
				linkHandler.browse(uri);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			
		} else if (linkHandler.canBrowse() && URI.class.isAssignableFrom(valueType)) {
			try {
				final URI uri = (URI)value;
				linkHandler.browse(uri);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			
		} else if (linkHandler.canOpen() && File.class.isAssignableFrom(valueType)) {
			try {
				final File file = (File)value;
				linkHandler.open(file);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			
		} else {
			navigator.seek(value);
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
	
	
	
	// --------------------------------
	private static class LinkHandler {
		
		private static final String xdgOpenCmd = "xdg-open";
		
		private static enum BrowseHandler {Desktop, Xdg}
		private static enum OpenHandler {Desktop, Xdg}
		
		private final BrowseHandler browseHandler;
		private final OpenHandler openHandler;
		
		private LinkHandler() {
			browseHandler = initBrowseHandler();
			openHandler = initOpenHandler();			
		}
		
		boolean canBrowse() {
			return browseHandler != null;
		}
		
		boolean canOpen() {
			return openHandler != null;
		}
		
		void browse(URI uri) throws IOException {
			switch (browseHandler) {
			case Desktop :
				Desktop.getDesktop().browse(uri);
				break;
			case Xdg:
				invokeXdgOpen(uri.toASCIIString());
				break;
			default:
				throw new UnsupportedOperationException("browse not supported.");					
			}
		}
		
		void open(File file)  throws IOException {
			switch (openHandler) {
			case Desktop :
				Desktop.getDesktop().open(file);
				break;
			case Xdg:
				invokeXdgOpen(file.getAbsolutePath());
				break;
			default:
				throw new UnsupportedOperationException("open not supported.");					
			}
		}
		
		
		private void invokeXdgOpen(String argument) {
			try {
				final String cmd = xdgOpenCmd + " " + argument;
				
				System.out.println("Invoke: " + cmd);
				
				Runtime.getRuntime().exec(cmd);
								
			} catch (IOException ioex) {
				throw new RuntimeException(ioex);
			}
		}


		private BrowseHandler initBrowseHandler() {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				return BrowseHandler.Desktop;
				
			} else {
				try {
					Runtime.getRuntime().exec(xdgOpenCmd);
					return BrowseHandler.Xdg;					
				} catch (IOException ioex) {
					// xdg-open not found
				}				
			}
			return null;
		}
		
		private OpenHandler initOpenHandler() {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
				return OpenHandler.Desktop;
				
			} else {
				try {
					Runtime.getRuntime().exec(xdgOpenCmd);
					return OpenHandler.Xdg;					
				} catch (IOException ioex) {
					// xdg-open not found
				}				
			}
			return null;
		}
	}
	
}
