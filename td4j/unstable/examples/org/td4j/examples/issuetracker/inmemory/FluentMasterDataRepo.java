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

import java.util.List;

import org.td4j.core.reflect.Operation;
import org.td4j.core.tk.ObjectTK;

/**
 * This repo delegates to the MasterDataRepo.
 * It differs from MasterDataRepo in the methods return type, often returning
 * the IssueTemplate instead of the created object. This allows a better flow
 * of user interactions.
 * 
 * @author mira
 *
 */
public class FluentMasterDataRepo {

	private final MasterDataFactory delegate;
	
	public FluentMasterDataRepo(MasterDataFactory delegate) {
		this.delegate = ObjectTK.enforceNotNull(delegate, "delegate");
	}
	
	@Operation(paramNames={"name"})
	public IssueTemplate createTemplate(String name) {
		return delegate.createTemplate(name);
	}
	
	@Operation(paramNames={"template", "name"})
	public IssueTemplate createSeverity(IssueTemplate template, String name) {
		delegate.createSeverity(template, name);
		return template;
	}
	
	@Operation(paramNames={"template", "name", "resolved"})
	public IssueTemplate createStatus(IssueTemplate template, String name, boolean resolved) {
		delegate.createStatus(template, name, resolved);
		return template;
	}
	
	@Operation(paramNames={"template", "name"})
	public IssueTemplate createModule(IssueTemplate template, String name) {
		delegate.createModule(template, name);
		return template;
	}
	
	@Operation(paramNames={"template", "name"})
	public IssueContainer createContext(IssueTemplate template, String name) {
		return delegate.createIssueContainer(template, name);
	}
	
	@Operation
	public List<IssueTemplate> findAllTemplates() {
		return delegate.getRepo().getAll(IssueTemplate.class);
	}
	
	@Operation
	public List<IssueContainer> findAllContexts() {
		return delegate.getRepo().getAll(IssueContainer.class);
	}
	
}
