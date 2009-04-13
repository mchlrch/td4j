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

package org.td4j.core.internal.binding.model.converter;

import org.td4j.core.tk.ObjectTK;

class ReverseConverter<A, B> implements IConverter<A, B> {

  private final IConverter<B, A> delegate;

  ReverseConverter(IConverter<B, A> delegate) {
    this.delegate = ObjectTK.enforceNotNull(delegate, "delegate");
  }

  @Override
  public boolean canConvert() {
    return delegate.canUnconvert();
  }

  @Override
  public boolean canUnconvert() {
    return delegate.canConvert();
  }

  public B convert(A from) {
    return delegate.unconvert(from);
  };

  public A unconvert(B from) {
    return delegate.convert(from);
  };

}
