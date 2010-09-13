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

package org.td4j.examples.issuetracker.inmemory;

import org.td4j.core.tk.ObjectTK;

/**
 * Responsibilities of the MasterDataFactory:
 * 1) create instance
 * 2) wire back-reference, if appropriate
 * 3) register instance in repo
 */
public class MasterDataFactory {
	
	private final EntityRepo repo;
	
	public MasterDataFactory(EntityRepo repo) {
		this.repo = ObjectTK.enforceNotNull(repo, "repo");
	}
	
	EntityRepo getRepo() { return repo; }
	
	public IssueTemplate createTemplate(String name) {
		final IssueTemplate template = new IssueTemplate();
		template.setName(name);
		repo.put(IssueTemplate.class, template);
		return template;
	}
	
	public Severity createSeverity(IssueTemplate template, String name) {
		final Severity severity = new Severity();
		severity.setTemplate(template);
		severity.setName(name);
		
		template.addSeverity(severity);
		
		repo.put(Severity.class, severity);
		return severity;
	}
		
	public Status createStatus(IssueTemplate template, String name, boolean resolved) {
		final Status status = new Status();
		status.setTemplate(template);
		status.setName(name);
		status.setResolved(resolved);
		
		template.addStatus(status);
		
		repo.put(Status.class, status);
		return status;
	}
	
	public Module createModule(IssueTemplate template, String name) {
		final Module module = new Module();
		module.setTemplate(template);
		module.setName(name);
		
		template.addModule(module);
		
		repo.put(Module.class, module);
		return module;
	}
	
	public IssueContainer createIssueContainer(IssueTemplate template, String name) {
		final IssueContainer container = new IssueContainer(name, template);
		
		template.addDerivate(container);
		
		repo.put(IssueContainer.class, container);
		return container;
	}
	
}
