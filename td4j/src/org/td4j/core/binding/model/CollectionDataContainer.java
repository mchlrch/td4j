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

package org.td4j.core.binding.model;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.internal.binding.model.CollectionDataContainerConnector;
import org.td4j.core.model.Observable;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;



public class CollectionDataContainer<T> extends Observable {

	private final Class<T> contentType;	
	private final boolean canRead;
	private final boolean canWrite;
	private final String propertyName;

	private List<T> content;

	public CollectionDataContainer(Class<T> contentType, String propertyName) {
		this(contentType, propertyName, true, true);
	}

	public CollectionDataContainer(Class<T> contentType, String propertyName, boolean canRead, boolean canWrite) {
		this.contentType = ObjectTK.enforceNotNull(contentType, "contentType");
		this.propertyName = StringTK.enforceNotEmpty(propertyName, "propertyName");

		this.canRead = canRead;
		this.canWrite = canWrite;
	}

	public Class<T> getContentType() {
		return contentType;
	}
	
	public Class<?> getCollectionType() {
		return List.class;
	}

	public boolean canRead() {
		return canRead;
	}

	public boolean canWrite() {
		return canWrite;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> newContent) {
		if (this.content != null && ObjectTK.equal(this.content, newContent)) return;

		if (newContent != null) {
			this.content =  newContent;
		} else {
			this.content = new ArrayList<T>();
		}
		changeSupport.fireStateChange();
	}
	
	public void clearContent() {
		this.content = new ArrayList<T>();
		changeSupport.fireStateChange();
	}
	
	public void addContent(T newContent) {
		ObjectTK.enforceNotNull(newContent, "newContent");
		if (this.content == null) this.content = new ArrayList<T>(); 
		
		content.add(newContent);
		changeSupport.fireStateChange();
	}
	
	public void removeContent(T contentToRemove) {
		ObjectTK.enforceNotNull(contentToRemove, "contentToRemove");
		if (this.content == null) return; 
		
		content.remove(contentToRemove);
		changeSupport.fireStateChange();
	}

	public CollectionDataProxy createProxy() {
		final CollectionDataContainerConnector con = new CollectionDataContainerConnector(getContentType(), getCollectionType(), getPropertyName());
		final CollectionDataProxy proxy = con.createProxy();
		proxy.setModel(this);
		return proxy;
	}

	@Override
	public String toString() {
		return propertyName;
	}

}
