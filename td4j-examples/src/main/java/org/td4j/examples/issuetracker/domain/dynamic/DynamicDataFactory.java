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

package org.td4j.examples.issuetracker.domain.dynamic;

import org.td4j.core.reflect.Operation;
import org.td4j.examples.issuetracker.EntityRepo;
import org.td4j.examples.issuetracker.domain.master.IssueContainerTemplate;

import ch.miranet.commons.ObjectTK;

/**
 * Responsibilities of the DynamicDataFactory:
 * 1) create instance
 * 2) wire back-reference, if appropriate
 * 3) register instance in repo
 */
public class DynamicDataFactory {
	
	private final EntityRepo repo;
	
	public DynamicDataFactory(EntityRepo repo) {
		this.repo = ObjectTK.enforceNotNull(repo, "repo");
	}
	
	@Operation
	public Issue createIssue(IssueContainer container) {
		final Issue issue = new Issue(container);
		
		container.addIssue(issue);
		
		repo.put(Issue.class, issue);
		return issue;
	}

	// exposed through FluentMasterDataFactory, because a template is needed as context for creation
	public IssueContainer createIssueContainer(IssueContainerTemplate template, String name) {
		final IssueContainer container = new IssueContainer(name, template);
		
		template.addDerivate(container);
		
		repo.put(IssueContainer.class, container);
		return container;
	}
	
}
