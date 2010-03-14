/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2010 Michael Rauch

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

package org.td4j.core.env;

public class SvcRepo {
	
	private static SvcRepository delegate;
	
	public static void init(SvcRepository repository) {
		if (repository == null) throw new NullPointerException("repository");
		if (delegate != null) throw new IllegalStateException("already initialized");
		
		delegate = repository;
	}
	
	public static <T> T getService(Class<T> svcDef) {
		return delegate.getService(svcDef);
	}
	
	public static <T> T requireService(Class<T> svcDef) {
		final T svc = getService(svcDef);
		if (svc == null) {
			throw new IllegalStateException("Required Service not available: " + svcDef);
		} else {
			return svc;
		}
	}
	
	
	public static SvcRepository getRepositoryDelegate() {
		return delegate;
	}	

}
