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

import java.util.List;
import java.util.Set;

import org.td4j.core.metamodel.FeatureKey;
import org.td4j.core.metamodel.MetaClass;
import org.td4j.core.reflect.ListProperty;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.tk.ObjectTK;

public class JavaMetaClass<T> extends MetaClass {

	private final Class<T> javaClass;
	private final int hashCode;
	
	private final DefaultFeatureRepository featRepo = new DefaultFeatureRepository();

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

	// === [ open scope to public ] ===================================
	
	@Override
	public void setScalarProperties(List<ScalarProperty> properties) {
		super.setScalarProperties(properties);
	}
	
	@Override
	public void setListProperties(List<ListProperty> properties) {
		super.setListProperties(properties);
	}

	// =============================================================

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
