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

package org.td4j.examples.issuetracker.memory.domain.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.ShowProperties;
import org.td4j.examples.issuetracker.memory.domain.NamedElement;
import org.td4j.examples.issuetracker.memory.domain.master.IssueContainerTemplate;

@Companions({ DynamicDataFactory.class, DynamicDataRepository.class })
public class IssueContainer extends NamedElement {

	private IssueContainerTemplate template;

	private List<Issue> issues = new ArrayList<Issue>();

	@SuppressWarnings("unused")
	private IssueContainer() {
	}

	IssueContainer(String name, IssueContainerTemplate template) {
		setName(name);
		setTemplate(template);
	}

	public IssueContainerTemplate getTemplate() {
		return template;
	}

	private void setTemplate(IssueContainerTemplate template) {
		this.template = template;
	}

	@ShowProperties({ "title", "status", "severity" })
	public List<Issue> getIssues() {
		return issues;
	}

	void addIssue(Issue issue) {
		issues.add(issue);
	}

}
