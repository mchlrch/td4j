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

package org.td4j.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;


public class SimplePopupApp extends JPanel {
	private static final long serialVersionUID = 1L;

	SimplePopupApp() {
		setLayout(new GridBagLayout());

		final JLabel addressLabel = new JLabel("address");
		final JTextField addressText = new JTextField("-address-");

		final Insets insets = new Insets(5, 5, 5, 5);
		add(addressLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
		add(addressText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 0, 0));

		addressText.addMouseListener(new PopupMouseAdapter(addressText));
	}


	private static class PopupMouseAdapter extends MouseAdapter {
		private final Component baseComp;
		private int count = 0;
		private Popup popup;

		PopupMouseAdapter(Component baseComp) {
			this.baseComp = baseComp;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (popup != null) return;

			final PopupFactory factory = PopupFactory.getSharedInstance();
			final JLabel popupLabel = new JLabel("pop-up label " + ++count);
			popup = factory.getPopup(baseComp, popupLabel, 0, 0);
			popup.show();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (popup == null) return;

			popup.hide();
			popup = null;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final SimplePopupApp panel = new SimplePopupApp();
				final JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
