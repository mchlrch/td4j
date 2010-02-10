/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009 Michael Rauch

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

package org.td4j.core.tk.filter;

import org.td4j.core.tk.ArrayTK;
import org.td4j.core.tk.IFilter;

public class CompositeFilter<T> implements IFilter<T> {

    private final IFilter<T>[] delegates;
  
    public CompositeFilter(IFilter<T>... delegates) {
      this.delegates = ArrayTK.enforceNotEmpty(delegates, "delegates");      
    }
  
	public boolean accept(T element) {
	  boolean accepted = true;
	  for (int i=0, n=delegates.length; accepted && i<n; i++) {
	    accepted = delegates[i].accept(element);
	  }
	  return accepted;
	}

}