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

package org.td4j.core.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


import ch.miranet.commons.ObjectTK;
import ch.miranet.commons.StringTK;



public class ObservableList<E> implements List<E> {

	private final List<E> delegate;

	private final ChangeSupport changeSupport;
	private final String propertyName;

	public ObservableList(List<E> delegate, ChangeSupport changeSupport) {
		this(changeSupport, null, delegate);
	}

	public ObservableList(List<E> delegate, ChangeSupport changeSupport, String propertyName) {
		this(changeSupport, propertyName, delegate);
		ObjectTK.enforceNotNull(propertyName, "propertyName");
	}

	private ObservableList(ChangeSupport changeSupport, String propertyName, List<E> delegate) {
		this.delegate = ObjectTK.enforceNotNull(delegate, "delegate");
		this.changeSupport = ObjectTK.enforceNotNull(changeSupport, "changeSupport");
		this.propertyName = propertyName;
	}

	private void fireChange() {
		if (StringTK.isEmpty(propertyName)) {
			changeSupport.fireStateChange();
		} else {
			changeSupport.fireLazyPropertyChange(propertyName);
		}
	}

	public int size() {
		return delegate.size();
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public boolean add(E e) {
		boolean b = delegate.add(e);
		fireChange();
		return b;
	}

	public void add(int index, E element) {
		delegate.add(index, element);
		fireChange();
	}

	public boolean addAll(Collection<? extends E> c) {
		boolean b = delegate.addAll(c);
		fireChange();
		return b;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		boolean b = delegate.addAll(index, c);
		fireChange();
		return b;
	}

	public void clear() {
		delegate.clear();
		fireChange();
	}

	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	public E get(int index) {
		return delegate.get(index);
	}

	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	public Iterator<E> iterator() {
		return delegate.iterator();
	}

	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return delegate.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return delegate.listIterator(index);
	}

	public E remove(int index) {
		E element = delegate.remove(index);
		fireChange();
		return element;
	}

	public boolean remove(Object o) {
		boolean b = delegate.remove(o);
		fireChange();
		return b;
	}

	public boolean removeAll(Collection<?> c) {
		boolean b = delegate.removeAll(c);
		fireChange();
		return b;
	}

	public boolean retainAll(Collection<?> c) {
		boolean b = delegate.retainAll(c);
		fireChange();
		return b;
	}

	public E set(int index, E element) {
		E el = delegate.set(index, element);
		fireChange();
		return el;
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

}
