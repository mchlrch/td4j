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

package org.td4j.core.internal.binding.model.converter;

public class String2DoubleConverter extends String2XYConverter {

	public String2DoubleConverter(Class<?> targetType, Object nullValue) {
		super(targetType, nullValue);
		
		if (targetType != double.class && targetType != Double.class) throw new IllegalArgumentException("targetType");
	}
	
  @Override
  public Object convert(Object from) {
  	if (from instanceof String) {
  		final String s = (String)from;
  		if ( ! s.trim().isEmpty()) return Double.parseDouble(s);
  	}
  	
    return nullValue;
  }

  @Override
  public Object unconvert(Object from) {
  	if (from instanceof Double) return Double.toString( (Double)from );
  	
    return null;
  }

}
