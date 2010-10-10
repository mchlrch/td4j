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

package org.td4j.core.tk.feature;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class FeatureRepository implements FeatureProvider {
	
	private final Map<FeatureKey<?>, Object> featureMap = new HashMap<FeatureKey<?>, Object>();

	public <T extends Object> void putFeature(FeatureKey<T> key, T feature) {
		featureMap.put(key, feature);
	}
	
	public <T extends Object> T removeFeature(FeatureKey<T> key) {
		return (T) featureMap.remove(key);
	}

	public <T> T getFeature(FeatureKey<T> key) {
		final Object feature = featureMap.get(key);
		return (T) feature;
	}

	public Set<FeatureKey<?>> getFeatureKeys() {
		return Collections.unmodifiableSet(featureMap.keySet());
	}

}
