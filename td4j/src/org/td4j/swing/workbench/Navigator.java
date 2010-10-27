/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2010 Michael Rauch

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

package org.td4j.swing.workbench;

import org.td4j.swing.workbench.Editor.EditorContent;

import ch.miranet.commons.ObjectTK;

public class Navigator {

	private final Workbench workbench;

	Navigator(Workbench workbench) {
		this.workbench = ObjectTK.enforceNotNull(workbench, "workbench");
	}

	public void seek(Class<?> cls) {
		workbench.show(cls);
	}

	public void seek(Object obj) {
		workbench.show(obj);
	}
	
	public void seek(EditorContent editorContent) {
		workbench.show(editorContent);
	}

  public boolean isTypeNavigatable(Class<?> type) {
    return ! isTypePrimitive(type);
  }
  
  protected boolean isTypePrimitive(Class<?> type) {
    return type.isPrimitive()
    || type == Boolean.class 
    || type == Byte.class
    || type == Character.class
    || type == Short.class
    || type == Integer.class
    || type == Long.class
    || type == Float.class
    || type == Double.class
    
    || type == String.class
    || type == Class.class;
  }

}
