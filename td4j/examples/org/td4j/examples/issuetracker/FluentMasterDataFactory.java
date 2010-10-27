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

package org.td4j.examples.issuetracker;

import org.td4j.core.reflect.Operation;
import org.td4j.examples.issuetracker.domain.dynamic.DynamicDataFactory;
import org.td4j.examples.issuetracker.domain.dynamic.IssueContainer;
import org.td4j.examples.issuetracker.domain.master.IssueContainerTemplate;
import org.td4j.examples.issuetracker.domain.master.MasterDataFactory;

import ch.miranet.commons.ObjectTK;

/**
 * This factory basically delegates to the MasterDataFactory.
 * 
 * It differs from MasterDataFactory in the methods return type, often returning
 * the IssueTemplate instead of the created object. This way, the user stays with
 * the IssueTemplate form to do the master-data management.
 * 
 * @author mira
 */
public class FluentMasterDataFactory {

	private final MasterDataFactory master;
	private final DynamicDataFactory dynamic;
	
	public FluentMasterDataFactory(MasterDataFactory master, DynamicDataFactory dynamic) {
		this.master  = ObjectTK.enforceNotNull(master, "master");
		this.dynamic = ObjectTK.enforceNotNull(dynamic, "dynamic");
	}
	
	@Operation(paramNames={"name"})
	public IssueContainerTemplate createTemplate(String name) {
		return master.createTemplate(name);
	}
	
	@Operation(paramNames={"template", "name"})
	public IssueContainerTemplate createSeverity(IssueContainerTemplate template, String name) {
		master.createSeverity(template, name);
		return template;
	}
	
	@Operation(paramNames={"template", "name", "closed"})
	public IssueContainerTemplate createStatus(IssueContainerTemplate template, String name, boolean closed) {
		master.createStatus(template, name, closed);
		return template;
	}
		
	@Operation(paramNames={"template", "name"})
	public IssueContainer createContainer(IssueContainerTemplate template, String name) {
		return dynamic.createIssueContainer(template, name);
	}

}
