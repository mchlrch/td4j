/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009 Michael Rauch

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

import java.util.List;

import org.td4j.core.reflect.ListProperty;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.tk.ObjectTK;

public class OpenJavaClass<T> extends OpenClass {
	
	private final Class<T> javaClass;
	
	public OpenJavaClass(OpenClassRepository repository, Class<T> javaClass, boolean primitive) {
		super(repository, javaClass.getPackage().getName(), javaClass.getName(), primitive);
		this.javaClass = ObjectTK.enforceNotNull(javaClass, "javaClass");
	}
	
	public Class<T> getJavaClass() {
		return javaClass;
	}

	
	void addScalarProperty(ScalarProperty property) {
		addFeature(ScalarProperty.class, property.getName(), property);
	}
	
	void addListProperty(ListProperty property) {
		addFeature(ListProperty.class, property.getName(), property);
	}
	
	public List<ScalarProperty> getScalarProperties() {
		return getFeatures(ScalarProperty.class, ScalarProperty.class);
	}
	
	public List<ListProperty> getListProperties() {
		return getFeatures(ListProperty.class, ListProperty.class);
	}

}
