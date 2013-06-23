/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2012 Michael Rauch

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

import javax.swing.JList;

import org.td4j.core.binding.model.IndividualDataContainer;
import org.td4j.core.binding.model.IndividualDataProxy;

public class SelectionContainer<T> {

	private final IndividualDataContainer<T> dataContainer;
	private final IndividualDataProxy dataProxy;
	private SelectionController selectionController;

	public SelectionContainer(Class<T> contentType, String propertyName) {
		this.dataContainer = new IndividualDataContainer<T>(contentType, propertyName);
		this.dataProxy = this.dataContainer.createProxy();
	}

	public IndividualDataContainer<T> getDataContainer() {
		return dataContainer;
	}

	public IndividualDataProxy getDataProxy() {
		return dataProxy;
	}

	public void captureSelection(JList<?> list) {
		this.assertNotYetCapturing();

		this.selectionController = SelectionController.createSelectionController(list, this.dataProxy);
	}

	public void captureSelection(TableController tableController) {
		this.assertNotYetCapturing();

		this.selectionController = SelectionController.createSelectionController(tableController, this.dataProxy);
	}

	private void assertNotYetCapturing() {
		if (this.selectionController != null) {
			throw new IllegalStateException(String.format("%s is already capturing %s.", getClass().getName(), this.dataProxy.getName()));
		}
	}

}
