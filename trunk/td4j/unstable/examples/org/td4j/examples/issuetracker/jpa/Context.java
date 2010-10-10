package org.td4j.examples.issuetracker.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.td4j.core.reflect.Companions;

@Companions(IssueRepo.class)
@Entity
public class Context extends NamedElement {
	
	@ManyToOne private IssueTemplate template;
	
	@OneToMany(mappedBy="context", cascade=CascadeType.ALL)	
	private List<Issue> issues;
	
	@SuppressWarnings("unused") private Context() {}
	Context(String name, IssueTemplate template) {
		setName(name);
		setTemplate(template);
	}
	
	public IssueTemplate getTemplate()               { return template; }
	private void setTemplate(IssueTemplate template) { this.template = template; }
	
	public List<Issue> getIssues() { return issues; }
	void addIssue(Issue issue)     { issues.add(issue); }
	
}
