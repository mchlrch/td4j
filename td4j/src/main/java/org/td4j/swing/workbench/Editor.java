/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008 Michael Rauch

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import ch.miranet.commons.TK;


public abstract class Editor {

	private final Workbench workbench;
	private final Class<?> contentType;

	protected Editor(Workbench workbench, Class<?> contentType) {
		this.workbench = TK.Objects.assertNotNull(workbench, "workbench");
		this.contentType = TK.Objects.assertNotNull(contentType, "contentType");
	}

	public Workbench getWorkbench() {
		return workbench;
	}
	
	public Class<?> getContentType() {
		return contentType;
	}

	public void setContent(Collection<?> instances, Object mainObject) {
		setContent(new EditorContent(contentType, instances, mainObject));
	}
	
	public void setContent(Collection<?> instances) {
		setContent(new EditorContent(contentType, instances));
	}
	
	public void setContent(Object mainObject) {
		setContent(new EditorContent(contentType, mainObject));
	}	
	
	
	public abstract void setContent(EditorContent content);
	
	public abstract EditorContent getContent();

	public abstract JComponent getComponent();
	
	
	
	public static class EditorContent {
		
		private final Class<?> contentType;
		private final List<Object> instances;
		private final Object mainObject;
		
		private static List<Object> wrapInList(Object obj) {
			final List<Object> instances = new ArrayList<Object>();
			if (obj != null) instances.add(obj);
			
			return instances;			
		}
		
		private static Object getFirstElement(Collection<?> instances) {
			final Iterator<?> it = instances.iterator();
			return it.hasNext() ? it.next() : null;
		}
		
		public EditorContent(Class<?> contentType, Object mainObject) {
			this(contentType, wrapInList(mainObject), mainObject);
		}
		
		public EditorContent(Class<?> contentType, Collection<?> instances) {
			this(contentType, instances, getFirstElement(instances));
		}
		
		public EditorContent(Class<?> contentType, Collection<?> instances, Object mainObject) {
			this.contentType = TK.Objects.assertNotNull(contentType, "contentType");
			this.instances = new ArrayList<Object>(TK.Objects.assertNotNull(instances, "instances"));
			this.mainObject = mainObject;
		}
		
		public Class<?> getContentType() {
			return contentType;
		}
		
		public List<?> getInstances() {
			return instances;
		}
		
		public Object getMainObject() {
			return mainObject;
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			for (Object obj : instances) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(obj);
			}
			sb.append("}");
			
			sb.insert(0, " {");
			sb.insert(0, mainObject);
			sb.insert(0, ": ");
			sb.insert(0, contentType.getName());
			return sb.toString();
		}
	}

}
