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

import java.util.Collections;
import java.util.List;

import org.td4j.core.internal.reflect.AbstractExecutable;
import org.td4j.core.metamodel.container.IndividualProperties;
import org.td4j.core.metamodel.container.ListProperties;
import org.td4j.core.metamodel.container.Operations;
import org.td4j.core.metamodel.feature.IndividualPropertiesKey;
import org.td4j.core.metamodel.feature.ListPropertiesKey;
import org.td4j.core.metamodel.feature.OperationsKey;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.reflect.ListProperty;
import org.td4j.core.tk.feature.FeatureKey;
import org.td4j.core.tk.feature.FeatureProvider;



public abstract class MetaClass implements FeatureProvider {

	public abstract String getName();
	public abstract String getSimpleName();

	// TODO: model pkg structure
//	public abstract MetaPackage getPackage();

	
	public List<IndividualProperty> getIndividualProperties() {
		final IndividualProperties props = getFeature(IndividualPropertiesKey.ALL);
		if (props != null) {
			return Collections.unmodifiableList(props.get());
		} else {
			return Collections.emptyList();
		}
	}
	
	public IndividualProperty getIndividualProperty(String name) {
		final IndividualProperties props = getFeature(IndividualPropertiesKey.ALL);
		return props != null ? props.getByName(name) : null;
	}
	
	public List<ListProperty> getListProperties() {
		final ListProperties props = getFeature(ListPropertiesKey.ALL);
		if (props != null) {
			return Collections.unmodifiableList(props.get());
		} else {
			return Collections.emptyList();
		}
	}
	
	public ListProperty getListProperty(String name) {
		final ListProperties props = getFeature(ListPropertiesKey.ALL);
		return props != null ? props.getByName(name) : null;
	}
	
	public List<AbstractExecutable> getOperations() {
		final Operations ops = getFeature(OperationsKey.ALL);
		if (ops != null) {
			return Collections.unmodifiableList(ops.get());
		} else {
			return Collections.emptyList();
		}		
	}

	@Override
	public String toString() {
		return getName();
	}
	
	// ==========================================================================
	
	protected void setIndividualProperties(List<IndividualProperty> properties) {
		final IndividualProperties props = new IndividualProperties(properties);
		putFeature(IndividualPropertiesKey.ALL, props);
	}
	
	protected void setListProperties(List<ListProperty> properties) {
		final ListProperties props = new ListProperties(properties);
		putFeature(ListPropertiesKey.ALL, props);
	}
	
	protected void setOperations(List<AbstractExecutable> operations) {
		final Operations ops = new Operations(operations);
		putFeature(OperationsKey.ALL, ops);
	}

	protected abstract <T extends Object> void putFeature(FeatureKey<T> key, T feature);

}
