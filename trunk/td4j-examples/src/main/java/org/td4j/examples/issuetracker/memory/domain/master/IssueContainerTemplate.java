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

package org.td4j.examples.issuetracker.memory.domain.master;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.ShowProperties;
import org.td4j.examples.issuetracker.memory.FluentMasterDataFactory;
import org.td4j.examples.issuetracker.memory.domain.NamedElement;
import org.td4j.examples.issuetracker.memory.domain.dynamic.IssueContainer;

import ch.miranet.commons.TK;

@Companions({ FluentMasterDataFactory.class, MasterDataRepository.class })
public class IssueContainerTemplate extends NamedElement {

	private List<Severity>       severities = new ArrayList<Severity>();
	private List<Status>         stati      = new ArrayList<Status>();
	private List<IssueContainer> derivates  = new ArrayList<IssueContainer>();

	@ShowProperties("name")
	public List<Severity> getSeverities() {
		return severities;
	}

	@ShowProperties({ "name", "closed" })
	public List<Status> getStati() {
		return stati;
	}

	@ShowProperties("name")
	public List<IssueContainer> getDerivates() {
		return derivates;
	}

	void addSeverity(Severity severity) {
		severities.add(TK.Objects.assertNotNull(severity, "severity"));
	}

	void addStatus(Status status) {
		stati.add(TK.Objects.assertNotNull(status, "status"));
	}

	public void addDerivate(IssueContainer container) {
		derivates.add(TK.Objects.assertNotNull(container, "container"));
	}

}
