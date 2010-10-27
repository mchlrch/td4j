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

import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.metamodel.feature.MetaClassKey;

import ch.miranet.commons.container.FeatureKey;
import ch.miranet.commons.service.SvcProvider;

public class JavaMetaModel extends MutableMetaModel {
	
	private final JavaMetaClassBuilder metaClassBuilder;
	
	public JavaMetaModel(SvcProvider svcProvider) {
		this.metaClassBuilder = new JavaMetaClassBuilder(this, svcProvider);
	}
	
	@Override
	public <T> T getFeature(FeatureKey<T> key) {
		T feature = super.getFeature(key);
		
		// create metaClasses lazy
		if (feature == null && key instanceof MetaClassKey) {
			final MetaClassKey metaClassKey = (MetaClassKey) key;
			final Class<?> cls = metaClassKey.getJavaClass();			
			final JavaMetaClass<?> metaClass = metaClassBuilder.buildMetaClass(cls);
			feature = (T) metaClass;
		}
		
		return feature;
	}

	
	// ----------------------------------------------------------------------------------------
	
	// this method is necessary, as the metaModel normally creates MetaClass lazy, on demand
	MetaClass getMetaClassReadonly(Class<?> cls) {
		final MetaClassKey key = metaClassKey(cls);
		final MetaClass metaClass = super.getFeature(key);
		return metaClass;
	}
	
	void putMetaClass(JavaMetaClass<?> metaClass) {
		final MetaClassKey key = metaClassKey(metaClass.getJavaClass());
		putFeature(key, metaClass);
	}
	
}
