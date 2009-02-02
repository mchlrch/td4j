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

import javax.swing.JPanel;


public class GridBagPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int _C_ = GridBagConstraints.CENTER;
	public static final int _N_ = GridBagConstraints.NORTH;
	public static final int _S_ = GridBagConstraints.SOUTH;
	public static final int _W_ = GridBagConstraints.WEST;
	public static final int _E_ = GridBagConstraints.EAST;
	public static final int _NW = GridBagConstraints.NORTHWEST;
	public static final int _NE = GridBagConstraints.NORTHEAST;
	public static final int _SW = GridBagConstraints.SOUTHWEST;
	public static final int _SE = GridBagConstraints.SOUTHEAST;
	
	public static final int _none = GridBagConstraints.NONE;
	public static final int _horz = GridBagConstraints.HORIZONTAL;
	public static final int _vert = GridBagConstraints.VERTICAL;
	public static final int _both = GridBagConstraints.BOTH;

	public static enum Filler {Horizontal, Vertical, Both}
	
//	private final int outerGap = 5;
//	private final int labelGap = 10;
//	private final int colGap = 20;
//	private final int rowGap = 20;

	public GridBagPanel() {
		setLayout(new GridBagLayout());
	}
	
	public void add(Component comp, int gridx, int gridy, int anchor, int fill) {
		add(comp, gbc(gridx, gridy, anchor, fill));
	}

	public static GridBagConstraints gbc(int gridx, int gridy, int anchor, int fill) {
		final double weightx = fill == _none || fill == _vert ? 0.0 : 1.0;
		final double weighty = fill == _none || fill == _horz ? 0.0 : 1.0;
		return gbc(gridx, gridy, 1, 1, weightx, weighty, anchor, fill);
	}
	
	public static GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill) {

		// PEND: choose sensible insets according to last gbc() call and this one
		final Insets insets = new Insets(5, 5, 5, 5);
		return gbc(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
	}

	public static GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
		return new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady);
	}
	
	public void addFiller() {
		throw new IllegalStateException("not implemented.");
	}

}
