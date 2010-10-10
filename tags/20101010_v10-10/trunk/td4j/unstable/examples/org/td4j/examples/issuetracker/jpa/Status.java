package org.td4j.examples.issuetracker.jpa;

import javax.persistence.Entity;

@Entity
public class Status extends TemplateElement {

	private boolean resolved;
	
	public boolean isResolved()               { return resolved; }
	public void setResolved(boolean resolved) { this.resolved = resolved; }
	
}
