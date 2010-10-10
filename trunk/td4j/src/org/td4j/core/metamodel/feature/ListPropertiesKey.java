/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2010 Michael Rauch

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

package org.td4j.core.metamodel.feature;

import org.td4j.core.metamodel.container.ListProperties;
import org.td4j.core.tk.feature.FeatureKey;

public class ListPropertiesKey implements FeatureKey<ListProperties> {
	
	public static final ListPropertiesKey ALL = new ListPropertiesKey();
	
	private final int hashCode;
	
	private ListPropertiesKey() {
		this.hashCode = getClass().hashCode();
	}
	
	@Override
	public Class<ListProperties> getFeatureType() {
		return ListProperties.class;
	}
	
	// =============================================================

	@Override
	public String toString() {
		return getClass().getName() + ": ALL";
	}
	
	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ListPropertiesKey) {
			final ListPropertiesKey that = (ListPropertiesKey) other;
			return that.canEqual(this);

		} else {
			return false;
		}
	}

	public boolean canEqual(Object other) {
		return other instanceof ListPropertiesKey;
	}

}
