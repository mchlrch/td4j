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

package org.td4j.core.metamodel;

import java.util.HashMap;
import java.util.Map;

import org.td4j.core.metamodel.feature.MetaClassKey;

import ch.miranet.commons.TK;
import ch.miranet.commons.container.FeatureProvider;


public abstract class MetaModel implements FeatureProvider, MetaClassProvider {
	
	private final Map<Class<?>, MetaClassKey> keyCache = new HashMap<Class<?>, MetaClassKey>();
	
	@Override
	public MetaClass getMetaClass(Class<?> cls) {
		TK.Objects.assertNotNull(cls, "cls");		
		final MetaClassKey key = metaClassKey(cls);		
		return getFeature(key);
	}
	
	protected MetaClassKey metaClassKey(Class<?> cls) {
		MetaClassKey key = keyCache.get(cls);
		if (key == null) {
			key = new MetaClassKey(cls);
			keyCache.put(cls, key);
		}
		
		return key;		
	}
	
}
