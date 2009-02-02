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

package org.td4j.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.td4j.core.internal.model.ChangeEventImpl;
import org.td4j.core.tk.ArrayTK;
import org.td4j.core.tk.IFilter;



public class ChangeSupport {

	private final Object source;
	private final Map<IObserver, IFilter<ChangeEvent>> observers = new HashMap<IObserver, IFilter<ChangeEvent>>();

	public ChangeSupport(Object source) {
		if (source == null) throw new NullPointerException("source");

		this.source = source;
	}

	public ChangeEvent preparePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (isEqual(oldValue, newValue)) return null;

		return preparePropertyChange(null, propertyName, oldValue, newValue);
	}

	public ChangeEvent preparePropertyChange(final ChangeEvent event, String propertyName, Object oldValue, Object newValue) {
		if (isEqual(oldValue, newValue)) return event;

		ChangeEventImpl eventImpl;

		if (event != null) {
			if ( ! (event instanceof ChangeEventImpl)) throw new IllegalArgumentException("multi propertyChanges only supported in ChangeEventImpl");
			eventImpl = (ChangeEventImpl) event;

		} else {
			eventImpl = new ChangeEventImpl(source, ChangeEvent.Type.PropertyChange);
		}

		eventImpl.addChangeRecord(propertyName, oldValue, newValue);
		return eventImpl;
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		final ChangeEventImpl eventImpl = new ChangeEventImpl(source, ChangeEvent.Type.PropertyChange);
		eventImpl.addChangeRecord(propertyName, oldValue, newValue);
		fire(eventImpl);
	}

	public void fireLazyPropertyChange(String... propertyNames) {
		if (ArrayTK.isEmpty(propertyNames)) return;

		final ChangeEventImpl eventImpl = new ChangeEventImpl(source, ChangeEvent.Type.LazyPropertyChange);
		for (String pName : propertyNames) {
			eventImpl.addLazyPropertyChange(pName);
		}
		fire(eventImpl);
	}

	public void fireStateChange() {
		final ChangeEventImpl eventImpl = new ChangeEventImpl(source, ChangeEvent.Type.StateChange);
		fire(eventImpl);
	}

	public void fire(ChangeEvent event) {
		notifyObservers(observers, event);
		notifyObservers(flatObservers, event);
	}

	public void addObserver(IObserver observer) {
		observers.put(observer, null);
	}

	public void addObserver(IObserver observer, IFilter<ChangeEvent> eventFilter) {
		observers.put(observer, eventFilter);
	}

	public void removeObserver(IObserver observer) {
		observers.remove(observer);
	}

	protected static void notifyObservers(Map<IObserver, IFilter<ChangeEvent>> observers, ChangeEvent event) {
		final List<Entry<IObserver, IFilter<ChangeEvent>>> observerEntries = new ArrayList<Entry<IObserver,IFilter<ChangeEvent>>>(observers.entrySet());
		for (Entry<IObserver, IFilter<ChangeEvent>> entry : observerEntries) {
			final IFilter<ChangeEvent> filter = entry.getValue();
			if (filter == null || filter.accept(event)) {
				entry.getKey().observableChanged(event);
			}
		}
	}

	// ---------------------------------------------------------
	// these methods can be used to listen application-wide to changeEvents

	private static final Map<IObserver, IFilter<ChangeEvent>> flatObservers = new HashMap<IObserver, IFilter<ChangeEvent>>();

	public static void addFlatObserver(IObserver observer) {
		flatObservers.put(observer, null);
	}

	public static void addFlatObserver(IFilter<ChangeEvent> eventFilter, IObserver observer) {
		flatObservers.put(observer, eventFilter);
	}

	public static void removeFlatObserver(IObserver observer) {
		flatObservers.remove(observer);
	}

	// ---------------------------------------------------------

	// PEND: maybe move to support class 'Objects'
	public static boolean isEqual(Object o1, Object o2) {
		return o1 != null ? o1.equals(o2) : o2 == null;
	}

	public static boolean isNotEqual(Object o1, Object o2) {
		return ! isEqual(o1, o2);
	}

}
