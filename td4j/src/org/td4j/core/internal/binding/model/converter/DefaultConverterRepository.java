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

import java.util.HashMap;
import java.util.Map;

import org.td4j.core.tk.ObjectTK;

public class DefaultConverterRepository implements IConverterRepository {

  // PEND: use dependency injection
  // PEND: make configurable
  public static final IConverterRepository INSTANCE = new DefaultConverterRepository();

  private final Map<ConverterKey, IConverter> map = new HashMap<ConverterKey, IConverter>();

  public DefaultConverterRepository() {
    addConverter(String.class, Integer.class, new String2IntConverter());
    addConverter(String.class, int.class, new String2IntConverter());
  }

  private <A, B> void addConverter(Class<A> fromType, Class<B> toType, IConverter<A, B> converter) {
    ObjectTK.enforceNotNull(converter, "converter");
    map.put(new ConverterKey(fromType, toType), converter);

    final IConverter<B, A> reverseConverter = new ReverseConverter<B, A>(converter);
    map.put(new ConverterKey(toType, fromType), reverseConverter);
  }

  @Override
  public <A, B> IConverter<A, B> getConverter(Class<A> fromType, Class<B> toType) {
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
