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

package org.td4j.examples;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class AppLauncher {

	public static void launch(final JComponent content, final String appTitle) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JFrame frame = new JFrame(appTitle);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(content);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
