package org.td4j.examples.issuetracker.jpa;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class TemplateElement extends NamedElement {

	private IssueTemplate template;
	
	public IssueTemplate getTemplate()       { return template; }
	void setTemplate(IssueTemplate template) { this.template = template; }
	
}
