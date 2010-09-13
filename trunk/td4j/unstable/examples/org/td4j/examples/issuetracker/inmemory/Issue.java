package org.td4j.examples.issuetracker.inmemory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Issue {
	
	private String title;
	private String description;
	private Date reportedAt;
	private Date resolvedAt;
	
	private IssueContainer context;
	private Person reportedBy;
	private Person assignedTo;
	
	private Severity severity;
	private Status status;
	private Module module;
	
	private List<Comment> comments = new ArrayList<Comment>();	
	
	private Issue() {}	
	Issue(IssueContainer context) {
		setContext(context);
		setReportedAt(new Date());
	}
	
	public String getTitle()           { return title; }
	public String getDescription()     { return description; }
	public Date getReportedAt()        { return reportedAt; }
	public Date getResolvedAt()        { return resolvedAt; }
	public IssueContainer getContext()        { return context; }
	public Person getReportedBy()      { return reportedBy; }
	public Person getAssignedTo()      { return assignedTo; }
	public Severity getSeverity()      { return severity; }
	public Status getStatus()          { return status; }
	public Module getModule()          { return module; }
	public List<Comment> getComments() { return comments; }	
	
	public void setTitle(String title)             { this.title = title; }
	public void setDescription(String description) { this.description = description; }
	private void setReportedAt(Date reportedAt)    { this.reportedAt = reportedAt; }
	private void setResolvedAt(Date resolvedAt)    { this.resolvedAt = resolvedAt; }
	private void setContext(IssueContainer context)       { this.context = context; }
	
	@Override
	public String toString() {
		return title;
	}

}
