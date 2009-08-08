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

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.model.Observable;



public class TagFilter extends Observable {

	private List<String> level0TagName = new ArrayList<String>();
	private List<String> level1TagName = new ArrayList<String>();

	private List<Boolean> level0TagSelected = new ArrayList<Boolean>();
	private List<Boolean> level1TagSelected = new ArrayList<Boolean>();

	public TagFilter() {
		setLevel0();
		setLevel1();
	}

	private void setLevel0() {
		level0TagName.clear();
		level0TagName.add("L0-1");
		level0TagName.add("L0-2");
		level0TagName.add("L0-3");
		level0TagName.add("L0-4");

		level0TagSelected.clear();
		level0TagSelected.add(Boolean.FALSE);
		level0TagSelected.add(Boolean.FALSE);
		level0TagSelected.add(Boolean.FALSE);
		level0TagSelected.add(Boolean.FALSE);

		changeSupport.fireLazyPropertyChange("level0TagName");
		changeSupport.fireLazyPropertyChange("level0TagSelected");
	}

	private void setLevel1() {
		final StringBuffer prefix = new StringBuffer("L1-");
		for (int i = 0, n = 4; i < n; i++) {
			if (level0TagSelected.get(i).booleanValue()) {
				prefix.append(i);
			}
		}

		level1TagName.clear();
		level1TagName.add(prefix.toString() + "--1");
		level1TagName.add(prefix.toString() + "--2");
		level1TagName.add(prefix.toString() + "--3");
		level1TagName.add(prefix.toString() + "--4");

		level1TagSelected.clear();
		level1TagSelected.add(Boolean.FALSE);
		level1TagSelected.add(Boolean.FALSE);
		level1TagSelected.add(Boolean.FALSE);
		level1TagSelected.add(Boolean.FALSE);

		changeSupport.fireLazyPropertyChange("level1TagName");
		changeSupport.fireLazyPropertyChange("level1TagSelected");
	}

	public String getLevel0TagName(int index) {
		return level0TagName.get(index);
	}

	public String getLevel1TagName(int index) {
		return level1TagName.get(index);
	}

	public void setLevel0TagName(int index, String s) {
	} // PEND: remove

	public void setLevel1TagName(int index, String s) {
	} // PEND: remove

	public Boolean isLevel0TagSelected(int index) {
		return level0TagSelected.get(index);
	}

	public Boolean isLevel1TagSelected(int index) {
		return level1TagSelected.get(index);
	}

	public void setLevel0TagSelected(int index, Boolean b) {
		level0TagSelected.set(index, b);
		setLevel1();
		changeSupport.fireLazyPropertyChange("level0TagSelected");
	}

	public void setLevel1TagSelected(int index, Boolean b) {
		level1TagSelected.set(index, b);
		changeSupport.fireLazyPropertyChange("level1TagSelected");
	}

}
