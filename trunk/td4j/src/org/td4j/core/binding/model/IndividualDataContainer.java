/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010 Michael Rauch

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

import org.td4j.core.internal.binding.model.IndividualDataContainerConnector;
import org.td4j.core.internal.binding.model.converter.DefaultConverterRepository;
import org.td4j.core.internal.binding.model.converter.IConverter;
import org.td4j.core.model.Observable;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;



public class IndividualDataContainer<T> extends Observable {

	private final Class<T> contentType;
	private final boolean canRead;
	private final boolean canWrite;
	private final String propertyName;

	private T content;

	public IndividualDataContainer(Class<T> contentType, String propertyName) {
		this(contentType, propertyName, true, true);
	}

	public IndividualDataContainer(Class<T> contentType, String propertyName, boolean canRead, boolean canWrite) {
		this.contentType = ObjectTK.enforceNotNull(contentType, "contentType");
		this.propertyName = StringTK.enforceNotEmpty(propertyName, "propertyName");

		this.canRead = canRead;
		this.canWrite = canWrite;
	}

	public Class<T> getContentType() {
		return contentType;
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

	public T getContent() {
		return content;
	}

	public void setContent(T newContent) {
		if (ObjectTK.equal(this.content, newContent)) return;

		this.content = newContent;
		changeSupport.fireStateChange();
	}

	public IndividualDataProxy createProxy() {
		return createProxy( (IConverter) null);
	}
	
	public IndividualDataProxy createProxy(Class<?> targetValueType) {
		final Class<?> fromType = getContentType();
		final IConverter converter = DefaultConverterRepository.INSTANCE.getConverter(fromType, targetValueType);
		return createProxy(converter);
	}
	
	public IndividualDataProxy createProxy(IConverter converter) {
		final IndividualDataContainerConnector con = new IndividualDataContainerConnector(getContentType());
		final IndividualDataProxy proxy = new IndividualDataProxy(con, getPropertyName(), converter);
		proxy.setContext(this);

		return proxy;
	}

	@Override
	public String toString() {
		return propertyName;
	}

}
