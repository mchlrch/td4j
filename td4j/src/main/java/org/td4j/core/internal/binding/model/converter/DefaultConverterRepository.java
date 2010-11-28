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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ch.miranet.commons.ObjectTK;

public class DefaultConverterRepository implements IConverterRepository {

  // PEND: use dependency injection
  // PEND: make configurable
  public static final IConverterRepository INSTANCE = new DefaultConverterRepository();

  private final Map<ConverterKey, IConverter> map = new HashMap<ConverterKey, IConverter>();

  public DefaultConverterRepository() {
  	addConverter(String.class, Byte.class, new String2ByteConverter(Byte.class, getNullEquivalentFor(Byte.class)));
    addConverter(String.class, byte.class, new String2ByteConverter(byte.class, getNullEquivalentFor(byte.class)));
    
    addConverter(String.class, Short.class, new String2ShortConverter(Short.class, getNullEquivalentFor(Short.class)));
    addConverter(String.class, short.class, new String2ShortConverter(short.class, getNullEquivalentFor(short.class)));
  	
    addConverter(String.class, Integer.class, new String2IntConverter(Integer.class, getNullEquivalentFor(Integer.class)));
    addConverter(String.class, int.class,     new String2IntConverter(int.class,     getNullEquivalentFor(int.class)));
    
    addConverter(String.class, Long.class, new String2LongConverter(Long.class, getNullEquivalentFor(Long.class)));
    addConverter(String.class, long.class, new String2LongConverter(long.class, getNullEquivalentFor(long.class)));
    
    addConverter(String.class, Float.class, new String2FloatConverter(Float.class, getNullEquivalentFor(Float.class)));
    addConverter(String.class, float.class, new String2FloatConverter(float.class, getNullEquivalentFor(float.class)));
    
    addConverter(String.class, Double.class, new String2DoubleConverter(Double.class, getNullEquivalentFor(Double.class)));
    addConverter(String.class, double.class, new String2DoubleConverter(double.class, getNullEquivalentFor(double.class)));    
    
    addConverter(String.class, BigDecimal.class, new String2BigDecimalConverter());
  }
  
  @Override
  public Object getNullEquivalentFor(Class<?> type) {
  	if      (type == byte.class)    return (byte)  0;
  	else if (type == short.class)   return (short) 0;
  	else if (type == int.class)     return 0;
  	else if (type == long.class)    return 0L;
  	else if (type == float.class)   return 0.0F;
  	else if (type == double.class)  return 0.0D;
  	else if (type == boolean.class) return false;
  	
  	else return null;
  }

  private void addConverter(Class<?> fromType, Class<?> toType, IConverter converter) {
    ObjectTK.enforceNotNull(converter, "converter");
    map.put(new ConverterKey(fromType, toType), converter);

    final IConverter reverseConverter = new ReverseConverter(converter);
    map.put(new ConverterKey(toType, fromType), reverseConverter);
  }

  @Override
  public IConverter getConverter(Class<?> fromType, Class<?> toType) {
    final ConverterKey key = new ConverterKey(fromType, toType);
    return map.get(key);
  }

  // ---------------------------------------------------
  private static class ConverterKey {
    private final Class<?> clsFrom;
    private final Class<?> clsTo;

    private ConverterKey(Class<?> clsFrom, Class<?> clsTo) {
      this.clsFrom = ObjectTK.enforceNotNull(clsFrom, "clsFrom");
      this.clsTo = ObjectTK.enforceNotNull(clsTo, "clsTo");
    }

    @Override
    public int hashCode() {
      return 31 * clsFrom.hashCode() + clsTo.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof ConverterKey) {
        final ConverterKey that = (ConverterKey) obj;
        return this.clsFrom == that.clsFrom && this.clsTo == that.clsTo;

      }
      else {
        return false;
      }
    }
  }

}
