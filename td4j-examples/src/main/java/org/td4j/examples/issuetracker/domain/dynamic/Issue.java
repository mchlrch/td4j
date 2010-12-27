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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.ShowProperties;
import org.td4j.core.reflect.ShowPropertiesInEditorList;
import org.td4j.examples.issuetracker.domain.master.Severity;
import org.td4j.examples.issuetracker.domain.master.Status;

import ch.miranet.commons.ObjectTK;

@ShowPropertiesInEditorList({"title", "status", "severity"})
@Companions(CommentFactory.class)
public class Issue {
	
	private IssueContainer container;

	private String title;
	private String description;
	private Date reportedAt;
	private Date closedAt;	
	
	private Severity severity;
	private Status status;
	
	private List<Comment> comments = new ArrayList<Comment>();
	
	@SuppressWarnings("unused")
	private Issue() {}	
	Issue(IssueContainer container) {
		setContainer(container);
		setReportedAt(new Date());
	}
	
	public  IssueContainer getContainer()                         { return container; }
	private void           setContainer(IssueContainer container) { this.container = ObjectTK.enforceNotNull(container, "container"); }

	public String getTitle()             { return title; }
	public void   setTitle(String title) { this.title = title; }

	public String getDescription()                   { return description; }
	public void   setDescription(String description) { this.description = description; }

	public  Date getReportedAt()                { return reportedAt; }
	private void setReportedAt(Date reportedAt) { this.reportedAt = reportedAt; }
	
	public  Date getClosedAt()              { return closedAt; }
	private void setClosedAt(Date closedAt) { this.closedAt = closedAt; }
	
	public Severity       getSeverity()                  { return severity; }	
	public void           setSeverity(Severity severity) { this.severity = severity; }
	public List<Severity> getSeverityOptions()           { return container.getTemplate().getSeverities(); }
	
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		setClosedAt(isClosed() ? new Date() : null);
	}

	public List<Status> getStatusOptions() {
		return container.getTemplate().getStati();
	}
	
	public boolean isClosed() {
		return status != null && status.isClosed();
	}

	@ShowProperties({ "description", "commentedAt", "withLink" })
	public List<Comment> getComments()               { return comments; }
	       void          addComment(Comment comment) { comments.add(comment); }
	
	
	@Override
	public String toString() { return title; }

}
