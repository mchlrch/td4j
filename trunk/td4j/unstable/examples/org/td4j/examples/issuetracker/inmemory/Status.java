package org.td4j.examples.issuetracker.inmemory;

public class Status extends TemplateElement {

	private boolean resolved;
	
	public boolean isResolved()               { return resolved; }
	public void setResolved(boolean resolved) { this.resolved = resolved; }
	
}
