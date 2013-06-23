/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2012 Michael Rauch

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

package org.td4j.core.binding;

import ch.miranet.commons.TK;

public abstract class ContextSocketAdapter<I, O> implements ContextSocket<I> {

	private final Class<?> ctxTypeIn;
	private final ContextSocket<O> targetSocket;

	public ContextSocketAdapter(Class<?> ctxTypeIn, ContextSocket<O> targetSocket) {
		this.ctxTypeIn = TK.Objects.assertNotNull(ctxTypeIn, "ctxTypeIn");
		this.targetSocket = TK.Objects.assertNotNull(targetSocket, "targetSocket");
	}

	public Class<?> getContextType() {
		return this.ctxTypeIn;
	}

	public void setContext(I ctx) {
		final O targetCtx = this.adapt(ctx);
		this.targetSocket.setContext(targetCtx);
	}

	public I getContext() {
		throw new UnsupportedOperationException();
	}

	public void refreshFromContext() {
		this.targetSocket.refreshFromContext();
	}

	protected abstract O adapt(final I in);

}
