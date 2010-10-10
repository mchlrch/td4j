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

package org.td4j.core.binding;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.internal.binding.model.DataProxy;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeEventFilter;
import org.td4j.core.model.IObserver;
import org.td4j.core.model.Observable;
import org.td4j.core.tk.ObjectTK;



public class Mediator<T> extends Observable implements ContextSocket {

	private final List<ContextSocket> sockets = new ArrayList<ContextSocket>();
	private final Class<T> ctxType;
	private T ctx;

	private final LoopbackObserver loopbackObserver = new LoopbackObserver(this);

	public Mediator(Class<T> ctxType) {
		this.ctxType = ObjectTK.enforceNotNull(ctxType, "ctxType");
	}

	public Class<?> getContextType() {
		return ctxType;
	}

	public T getContext() {
		return ctx;
	}

	public void setContext(Object ctx) {
		
		// TODO: rename model-2-ctx für changeEvent nicht durchgeführt :: besser spezifisches Event für Mediator machen, nicht standard changeEvent
		final ChangeEvent changeEvent = changeSupport.preparePropertyChange("model", this.ctx, ctx);
		if (changeEvent == null) return;
		
		if (ctx != null && ! ctxType.isAssignableFrom(ctx.getClass())) {
			throw new IllegalArgumentException("type mismatch: " + ctx.getClass().getName() + " != " + ctxType.getName());
		}

		this.ctx = (T) ctx;
		
		for (ContextSocket delegate : sockets) {
			delegate.setContext(ctx);
		}
		
		changeSupport.fire(changeEvent);
	}

	public void refreshFromContext() {
		for (ContextSocket delegate : sockets) {
			delegate.refreshFromContext();
		}
		
		changeSupport.fireStateChange();
	}

	// interface to controller delegates
	// PEND: ev. mit callback methode auf delegate prüfen, ob delegate nicht schon
	// an anderem mediator angehängt ist (IndividualWidgetController.bindPlug())
	public void addContextSocket(ContextSocket delegate) {
		if ( ! sockets.contains(delegate)) {
			addLoopbackObserver(delegate);
			sockets.add(delegate);
			delegate.setContext(ctx);
		}
	}

	public void removeContextSocket(ContextSocket delegate) {
		sockets.remove(delegate);
		removeLoopbackObserver(delegate);
		delegate.setContext(null);
	}

	private void addLoopbackObserver(ContextSocket delegate) {
		if (delegate instanceof DataProxy) {
			final DataProxy proxy = (DataProxy) delegate;
			proxy.addObserver(loopbackObserver, new ChangeEventFilter(proxy, ChangeEvent.Type.Custom));
		}
	}

	private void removeLoopbackObserver(ContextSocket delegate) {
		if (delegate instanceof DataProxy) {
			final DataProxy proxy = (DataProxy) delegate;
			proxy.removeObserver(loopbackObserver);
		}
	}


	private static class LoopbackObserver implements IObserver {
		private final Mediator<?> mediator;

		private LoopbackObserver(Mediator<?> mediator) {
			this.mediator = ObjectTK.enforceNotNull(mediator, "mediator");
		}

		public void observableChanged(ChangeEvent event) {
			if (DataProxy.DataProxyChangeEvent.CustomEvent.ValueModified == event.getCustomPayload() && mediator.sockets.contains(event.getSource())) {
				mediator.refreshFromContext();
			}
		}
	}

}
