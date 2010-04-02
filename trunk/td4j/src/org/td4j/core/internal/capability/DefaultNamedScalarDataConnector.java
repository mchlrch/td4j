/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2009, 2010 Michael Rauch

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

package org.td4j.core.internal.capability;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.binding.model.ScalarDataConnector;
import org.td4j.core.reflect.ScalarProperty;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;

//TODO _0 move to pkg core.binding.model
public class DefaultNamedScalarDataConnector implements NamedScalarDataConnector {

	private final String name;
	private final ScalarDataConnector delegate;
	
	
	public static NamedScalarDataConnector[] createFromProperties(List<ScalarProperty> properties) {
		final List<NamedScalarDataConnector> result = new ArrayList<NamedScalarDataConnector>();
		for (ScalarProperty prop : properties) {
			result.add(new DefaultNamedScalarDataConnector(prop, prop.getName()));
		}
		return result.toArray(new NamedScalarDataConnector[result.size()]);
	}
	
	
	public DefaultNamedScalarDataConnector(ScalarDataConnector delegate, String name) {
		this.delegate = ObjectTK.enforceNotNull(delegate, "delegate");
		this.name = StringTK.enforceNotEmpty(name, "name");
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean canRead(Object ctx) {
		return delegate.canRead(ctx);
	}

	@Override
	public boolean canWrite(Object ctx) {
		return delegate.canWrite(ctx);
	}

	@Override
	public Class<?> getContextType() {
		return delegate.getContextType();
	}

	@Override
	public Class<?> getValueType() {
		return delegate.getValueType();
	}

	@Override
	public Object readValue(Object ctx) {
		return delegate.readValue(ctx);
	}

	@Override
	public void writeValue(Object ctx, Object val) {
		delegate.writeValue(ctx, val);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
