package org.td4j.examples.issuetracker.inmemory;

class TemplateElement extends NamedElement {

	private IssueTemplate template;
	
	public IssueTemplate getTemplate()       { return template; }
	void setTemplate(IssueTemplate template) { this.template = template; }
	
}
