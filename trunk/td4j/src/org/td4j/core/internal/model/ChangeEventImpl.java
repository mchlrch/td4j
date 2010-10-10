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

package org.td4j.core.internal.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.td4j.core.model.ChangeEvent;
import org.td4j.core.tk.ObjectTK;
import org.td4j.core.tk.StringTK;



public class ChangeEventImpl extends ChangeEvent {

	private final Object source;
	private final Type type;

	// for single-property events
	private String propertyName;
	private Object oldValue;
	private Object newValue;

	// for multi-property events
	private Map<String, ChangeRecord> changeMap;

	private Object customPayload;

	public ChangeEventImpl(Object source, Type type) {
		this.source = ObjectTK.enforceNotNull(source, "source");
		this.type = ObjectTK.enforceNotNull(type, "type");
	}

	@Override
	public Object getSource() {
		return source;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public String[] getPropertyNames() {
		if (propertyName != null) {
			return new String[] { propertyName };

		} else if (changeMap != null) {
			final String[] pNames = new String[changeMap.size()];
			int i = 0;
			for (Entry<String, ChangeRecord> changeEntry : changeMap.entrySet()) {
				pNames[i++] = changeEntry.getKey();
			}
			return pNames;

		} else {
			return new String[0];
		}

	}

	@Override
	public boolean contains(String propertyName) {
		if (this.propertyName != null) {
			return this.propertyName.equals(propertyName);

		} else if (changeMap != null) {
			return changeMap.containsKey(propertyName);

		} else {
			return false;
		}
	}

	@Override
	public Object getNewValue(String propertyName) {
		if ( ! contains(propertyName)) throw new IllegalArgumentException("unknown propertyName: " + propertyName);

		if (this.propertyName != null) {
			return newValue;

		} else {
			final ChangeRecord changeRecord = changeMap.get(propertyName);
			return changeRecord.newValue;
		}
	}

	@Override
	public Object getOldValue(String propertyName) {
		if ( ! contains(propertyName)) throw new IllegalArgumentException("unknown propertyName: " + propertyName);

		if (this.propertyName != null) {
			return oldValue;

		} else {
			final ChangeRecord changeRecord = changeMap.get(propertyName);
			return changeRecord.oldValue;
		}
	}

	public void addChangeRecord(String propertyName, Object oldValue, Object newValue) {
		addChangeRecord(propertyName, new ChangeRecord(oldValue, newValue));
	}

	public void addChangeRecord(String propertyName, ChangeRecord changeRecord) {
		if (StringTK.isEmpty(propertyName)) throw new IllegalArgumentException("propertyName is empty");

		if ( ! (type == Type.PropertyChange || type == Type.LazyPropertyChange)) throw new IllegalStateException("event type is NOT propertyChange");

		// 1st propertyName
		if (this.propertyName == null && changeMap == null) {
			this.propertyName = propertyName;
			if (changeRecord != null) {
				this.oldValue = changeRecord.oldValue;
				this.newValue = changeRecord.newValue;
			}

			// 2nd propertyName
		} else if (this.propertyName != null && changeMap == null) {

			// move existing changeRecord to the map
			changeMap = new HashMap<String, ChangeRecord>();
			if (type == Type.PropertyChange) {
				addChangeRecord(this.propertyName, this.oldValue, this.newValue);
			} else {
				addLazyPropertyChange(this.propertyName);
			}
			this.propertyName = null;
			this.oldValue = null;
			this.newValue = null;

			// add new changeRecord to the map
			changeMap.put(propertyName, changeRecord);

			// 3rd and more propertyName
		} else {
			changeMap.put(propertyName, changeRecord);
		}
	}

	public void addLazyPropertyChange(String propertyName) {
		addChangeRecord(propertyName, null);
	}

	@Override
	public Object getCustomPayload() {
		return customPayload;
	}

	public void setCustomPayload(Object payload) {
		this.customPayload = payload;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getType()).append(": ");
		for (String pName : getPropertyNames()) {
			sb.append(pName);
		}
		return sb.toString();
	}


	protected static class ChangeRecord {
		protected final Object oldValue;
		protected final Object newValue;

		protected ChangeRecord(Object oldValue, Object newValue) {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
	}

}
