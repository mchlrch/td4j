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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.td4j.core.internal.metamodel.container.MutableListProperties;
import org.td4j.core.internal.metamodel.feature.MutableListPropertiesKey;
import org.td4j.core.internal.reflect.AbstractExecutable;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.reflect.ListProperty;

import ch.miranet.commons.ObjectTK;
import ch.miranet.commons.container.FeatureKey;
import ch.miranet.commons.container.FeatureMap;

public class JavaMetaClass<T> extends MetaClass {

	private final Class<T> javaClass;
	private final int hashCode;
	
	private final FeatureMap featRepo = new FeatureMap();

	protected JavaMetaClass(Class<T> javaClass) {
		this.javaClass = ObjectTK.enforceNotNull(javaClass, "javaClass");
		this.hashCode = javaClass.hashCode();
	}

	public String getName() {
		return javaClass.getName();
	}

	public String getSimpleName() {
		return javaClass.getSimpleName();
	}

	public Class<T> getJavaClass() {
		return javaClass;
	}

	public <F extends Object> void putFeature(FeatureKey<F> key, F feature) {
		featRepo.putFeature(key, feature);
	}
	
	@Override
	public <F> F getFeature(FeatureKey<F> key) {
		return featRepo.getFeature(key);
	}
	
	@Override
	public Set<FeatureKey<?>> getFeatureKeys() {
		return featRepo.getFeatureKeys();
	}

	// === [ open scope for visibility in pkg ] ===================================
	
	@Override
	protected void setIndividualProperties(List<IndividualProperty> properties) {
		super.setIndividualProperties(properties);
	}	
	
	@Override
	protected void setOperations(List<AbstractExecutable> operations) {
		super.setOperations(operations);
	}

	// =============================================================
	
	void setMutableListProperties(List<MutableListProperty> properties) {		
		
		// MutableListProperties are only used temporary during MetaClass creation - they are removed later on
		final MutableListProperties props = new MutableListProperties(properties);
		putFeature(MutableListPropertiesKey.ALL, props);
		
		final List<ListProperty> listProperties = new ArrayList<ListProperty>();
		for (MutableListProperty prop : properties) {
			listProperties.add(prop);
		}
		super.setListProperties(listProperties);
	}
	
	List<MutableListProperty> getMutableListProperties() {
		final MutableListProperties props = getFeature(MutableListPropertiesKey.ALL);
		if (props != null) {
			return Collections.unmodifiableList(props.get());
		} else {
			return Collections.emptyList();
		}
	}
	
	protected void removeMutableListProperties() {
		featRepo.removeFeature(MutableListPropertiesKey.ALL);
	}
	
  //=============================================================

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof JavaMetaClass<?>) {
			final JavaMetaClass<?> that = (JavaMetaClass<?>) other;
			return that.canEqual(this)
					&& ObjectTK.equal(this.getJavaClass(), that.getJavaClass());

		} else {
			return false;
		}
	}

	public boolean canEqual(Object other) {
		return other instanceof JavaMetaClass<?>;
	}


}
