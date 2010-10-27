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

package org.td4j.core.internal.metamodel;

import java.util.Set;

import org.td4j.core.metamodel.MetaModel;

import ch.miranet.commons.container.FeatureKey;
import ch.miranet.commons.container.FeatureMap;

public class MutableMetaModel extends MetaModel {

	private final FeatureMap featRepo = new FeatureMap();

	protected <T extends Object> void putFeature(FeatureKey<T> key, T feature) {
		featRepo.putFeature(key, feature);
	}
	
	@Override
	public <T> T getFeature(FeatureKey<T> key) {
		return featRepo.getFeature(key);
	}
	
	@Override
	public Set<FeatureKey<?>> getFeatureKeys() {
		return featRepo.getFeatureKeys();
	}
}
