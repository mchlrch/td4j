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

package org.td4j.core.internal.binding.model;

public class ToStringConnector extends AbstractIndividualDataConnector {

  public ToStringConnector(Class<?> ctxType) {
    super(ctxType, String.class);
  }

  public boolean canRead()  { return true; }
  public boolean canWrite() { return false; }
  
  @Override
  public boolean canRead(Object ctx) {
    return ctx != null;
  }

  @Override
  public boolean canWrite(Object ctx) {
    return false;
  }

  @Override
  protected Object readValue0(Object ctx) throws Exception {
    return ctx.toString();
  }

  @Override
  protected void writeValue0(Object ctx, Object val) throws Exception {
    throw new UnsupportedOperationException("write not supported");
  }

  @Override
  public String toString() {
    return getContextType().getName() + "#toString";
  }
	
	@Override
	public boolean canEqual(Object other) {
		return other instanceof ToStringConnector;
	}
  
}
