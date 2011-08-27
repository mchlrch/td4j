/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009, 2010 Michael Rauch

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

package org.td4j.core.reflect;

import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.ListDataConnector;

import ch.miranet.commons.TK;

public class IndividualProperty implements IndividualDataConnector, Property {
	
	private final String name;
	private final IndividualDataConnector dataConnector;
	private final ListDataConnector choiceOptionsConnector;
	
	public IndividualProperty(String name, IndividualDataConnector dataConnector) {
		this(name, dataConnector, null);
	}
	
	public IndividualProperty(String name, IndividualDataConnector dataConnector, ListDataConnector choiceOptionsConnector) {
		this.name = TK.Strings.assertNotEmpty(name, "name");
		this.dataConnector = TK.Objects.assertNotNull(dataConnector, "dataConnector");
		this.choiceOptionsConnector = choiceOptionsConnector;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<?> getContextType() {
		return dataConnector.getContextType();
	}
	
	public Class<?> getValueType() {
		return dataConnector.getValueType();
	}
	
	public Object readValue(Object ctx) {
		TK.Objects.assertNotNull(ctx, "ctx");
		return dataConnector.readValue(ctx);
	}

	public void writeValue(Object ctx, Object val) {
		TK.Objects.assertNotNull(ctx, "ctx");
		dataConnector.writeValue(ctx, val);
	}

	public boolean canRead()   { return dataConnector.canRead();  }
	public boolean canWrite()  { return dataConnector.canWrite(); }
	public boolean canChoose() { return choiceOptionsConnector != null && choiceOptionsConnector.canRead(); }
	
	public boolean canRead(Object ctx) {
		if (ctx == null) return false;
		return dataConnector.canRead(ctx);		
	}

	public boolean canWrite(Object ctx) {
		if (ctx == null) return false;
		return dataConnector.canWrite(ctx);
	}	
	
	public boolean canChoose(Object ctx) {
		if (ctx == null || choiceOptionsConnector == null) return false;
		return choiceOptionsConnector.canRead(ctx);
	}
	
	public ListDataConnector getChoiceOptions() {
		return choiceOptionsConnector;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
