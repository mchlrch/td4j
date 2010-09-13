package org.td4j.examples.issuetracker.inmemory;

import java.util.ArrayList;
import java.util.List;

import org.td4j.core.reflect.Companions;
import org.td4j.core.reflect.ShowProperties;

@Companions(IssueRepo.class)
public class IssueContainer extends NamedElement {
	
	private IssueTemplate template;
		
	private List<Issue> issues = new ArrayList<Issue>();
	
	@SuppressWarnings("unused") private IssueContainer() {}
	IssueContainer(String name, IssueTemplate template) {
		setName(name);
		setTemplate(template);
	}
	
	public IssueTemplate getTemplate()               { return template; }
	private void setTemplate(IssueTemplate template) { this.template = template; }
	
	@ShowProperties({"title", "status", "severity", "module"})
	public List<Issue> getIssues() { return issues; }
	void addIssue(Issue issue)     { issues.add(issue); }
	
}
