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

import java.util.HashMap;
import java.util.Map;

import org.td4j.core.reflect.ModelInspector;
import org.td4j.core.tk.ObjectTK;

public class OpenClassRepository {

	private final ModelInspector modelInspector;
	
	private final Map<Class, OpenJavaClass> javaClassMap = new HashMap<Class, OpenJavaClass>();
	
	public OpenClassRepository(ModelInspector modelInspector) {
		this.modelInspector = ObjectTK.enforceNotNull(modelInspector, "modelInspector");		
	}
	
	void addClass(OpenClass openClass) {
		// TODO Auto-generated method stub
		// ensure uniqueness of class -> testcase		
	}
	
	public <T> OpenJavaClass<T> getOpenJavaClass(Class<T> cls) {
		OpenJavaClass<T> result = javaClassMap.get(cls);
		if (result != null) return result;
		
		// create new OpenClass
		result = new OpenJavaClass<T>(this, cls, false);
		javaClassMap.put(cls, result);
		
		// add properties
		for (org.td4j.core.reflect.ScalarProperty scalarProperty : modelInspector.getScalarProperties(cls)) {
			result.addScalarProperty(scalarProperty);
		}
		for (org.td4j.core.reflect.ListProperty listProperty : modelInspector.getListProperties(cls)) {
			result.addListProperty(listProperty);
		}
		
		return result;
	}

}
