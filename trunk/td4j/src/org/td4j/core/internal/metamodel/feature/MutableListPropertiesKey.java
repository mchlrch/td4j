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

package org.td4j.core.internal.metamodel.feature;

import org.td4j.core.internal.metamodel.container.MutableListProperties;
import org.td4j.core.tk.feature.FeatureKey;

public class MutableListPropertiesKey implements FeatureKey<MutableListProperties> {
	
	public static final MutableListPropertiesKey ALL = new MutableListPropertiesKey();
	
	private final int hashCode;
	
	private MutableListPropertiesKey() {
		this.hashCode = getClass().hashCode();
	}
	
	@Override
	public Class<MutableListProperties> getFeatureType() {
		return MutableListProperties.class;
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
		if (other instanceof MutableListPropertiesKey) {
			final MutableListPropertiesKey that = (MutableListPropertiesKey) other;
			return that.canEqual(this);

		} else {
			return false;
		}
	}

	public boolean canEqual(Object other) {
		return other instanceof MutableListPropertiesKey;
	}

}
