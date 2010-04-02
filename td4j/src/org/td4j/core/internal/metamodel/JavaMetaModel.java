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

import org.td4j.core.metamodel.FeatureKey;
import org.td4j.core.metamodel.feature.MetaClassKey;

public class JavaMetaModel extends MutableMetaModel {
	
	private final JavaModelInspector featureFactory = new JavaModelInspector(this);
	
	
	@Override
	public <T> T getFeature(FeatureKey<T> key) {
		T feature = super.getFeature(key);
		
		if (feature == null && key instanceof MetaClassKey) {
			final JavaMetaClass<?> metaClass = createMetaClass( (MetaClassKey) key);
			feature = (T) metaClass;
		}
		
		return feature;
	}


	private JavaMetaClass<?> createMetaClass(MetaClassKey key) {
		final Class<?> cls = key.getJavaClass();
		final JavaMetaClass<?> metaClass = new JavaMetaClass(cls);
		
		final JavaModelInspector.FeatureContainer features = featureFactory.createFeatures(cls);
		
		metaClass.setScalarProperties(features.scalarProperties);
		metaClass.setListProperties(features.listProperties);
		
		putFeature(key, metaClass);
		return metaClass;
	}

}
