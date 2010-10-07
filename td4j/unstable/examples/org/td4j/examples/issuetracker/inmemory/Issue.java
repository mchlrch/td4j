package org.td4j.examples.issuetracker.inmemory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.td4j.core.tk.ObjectTK;

public class Issue {
	
	private IssueContainer container;

	private String title;
	private String description;
	private Date reportedAt;
	private Date resolvedAt;	
	private Person reportedBy;
	private Person assignedTo;
	
	private Severity severity;
	private Status status;
	private Module module;
	
	private List<Comment> comments = new ArrayList<Comment>();	
	
	private Issue() {}	
	Issue(IssueContainer container) {
		setContainer(container);
		setReportedAt(new Date());
	}
	
	public IssueContainer getContainer() { return container; }

	public String getTitle()             { return title; }
	public String getDescription()       { return description; }
	public Date getReportedAt()          { return reportedAt; }
	public Date getResolvedAt()          { return resolvedAt; }
	public Person getReportedBy()        { return reportedBy; }
	public Person getAssignedTo()        { return assignedTo; }
	
	public Severity getSeverity()      { return severity; }
	public Status getStatus()          { return status; }
	public Module getModule()          { return module; }
	
	public List<Comment> getComments() { return comments; }	
	
	public void setSeverity(Severity severity) { this.severity = severity; }
	public List<Severity> getSeverityOptions() { return container.getTemplate().getSeverities(); }
	
	// -------------------------------------------------------------------
	private void setContainer(IssueContainer container) { this.container = ObjectTK.enforceNotNull(container, "container"); }
	public void setTitle(String title)                  { this.title = title; }
	public void setDescription(String description)      { this.description = description; }
	private void setReportedAt(Date reportedAt)         { this.reportedAt = reportedAt; }
	private void setResolvedAt(Date resolvedAt)         { this.resolvedAt = resolvedAt; }
	
	
	@Override
	public String toString() {
		return title;
	}

}
