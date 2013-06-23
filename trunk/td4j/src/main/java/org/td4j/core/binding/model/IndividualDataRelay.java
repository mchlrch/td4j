/*********************************************************************
  This file is part of td4j, see <http://td4j.org/>

  Copyright (C) 2008, 2009, 2010, 2013 Michael Rauch

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

package org.td4j.core.binding.model;

import org.td4j.core.binding.ContextSocket;
import org.td4j.core.model.ChangeEvent;
import org.td4j.core.model.ChangeEventFilter;
import org.td4j.core.model.IObserver;

import ch.miranet.commons.TK;


public class IndividualDataRelay<T> implements IObserver {

	private IndividualDataProxy master;
	private ContextSocket<T> slave;

	public static <A> IndividualDataRelay<A> createMasterSlaveRelay(IndividualDataProxy master, ContextSocket<A> slave) {
		return new IndividualDataRelay<A>(master, slave);
	}
	
	public IndividualDataRelay(IndividualDataProxy master, ContextSocket<T> slave) {
		this.master = TK.Objects.assertNotNull(master, "master");
		this.slave = TK.Objects.assertNotNull(slave, "slave");

		final Class<?> slaveType = slave.getContextType();
		if ( ! slaveType.isAssignableFrom(master.getValueType())) {
			throw new IllegalArgumentException("type mismatch: " + master.getValueType().getName() + " cannot be cascaded with " + slave.getContextType().getName());
		}

		master.addObserver(this, new ChangeEventFilter(master, ChangeEvent.Type.StateChange));

		updateSlave();
	}

	@SuppressWarnings("unchecked")
	private void updateSlave() {
		slave.setContext((T)master.readValue());
	}

	public void dispose() {
		if (master == null) throw new IllegalStateException("already disposed");

		master.removeObserver(this);
		master = null;
		slave = null;
	}

	public void observableChanged(ChangeEvent event) {
		updateSlave();
	}

}
