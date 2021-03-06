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

package org.td4j.core.internal.binding.model.converter;

import ch.miranet.commons.TK;

class ReverseConverter implements IConverter {

  private final IConverter delegate;

  ReverseConverter(IConverter delegate) {
    this.delegate = TK.Objects.assertNotNull(delegate, "delegate");
  }

  @Override
  public Class<?> getConversionTargetType() {
  	return delegate.getUnconversionTargetType();
  }
  
  @Override
  public Class<?> getUnconversionTargetType() {
  	return delegate.getConversionTargetType();
  }
  
  @Override
  public boolean canConvert() {
    return delegate.canUnconvert();
  }

  @Override
  public boolean canUnconvert() {
    return delegate.canConvert();
  }

  public Object convert(Object from) {
    return delegate.unconvert(from);
  };

  public Object unconvert(Object from) {
    return delegate.convert(from);
  };

}
