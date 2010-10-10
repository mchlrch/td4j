package org.td4j.examples.issuetracker.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Issue {
	
	@SuppressWarnings("unused") @Id @GeneratedValue private Integer id;
	
	private String title;
	private String description;
	@Temporal(TemporalType.TIMESTAMP) private Date reportedAt;
	@Temporal(TemporalType.TIMESTAMP) private Date resolvedAt;
	
	@ManyToOne private Context context;
	@ManyToOne private Person reportedBy;
	@ManyToOne private Person assignedTo;
	
	@ManyToOne private Severity severity;
	@ManyToOne private Status status;
	@ManyToOne private Module module;
	
	@OneToMany(mappedBy="issue", cascade=CascadeType.ALL)
	private List<Comment> comments;
	
	
	@SuppressWarnings("unused") private Issue() {}	
	Issue(Context context) {
		setContext(context);
		setReportedAt(new Date());
	}
	
	public String getTitle()           { return title; }
	public String getDescription()     { return description; }
	public Date getReportedAt()        { return reportedAt; }
	public Date getResolvedAt()        { return resolvedAt; }
	public Context getContext()        { return context; }
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
	private void setContext(Context context)       { this.context = context; }
	
	@Override
	public String toString() {
		return title;
	}

}
