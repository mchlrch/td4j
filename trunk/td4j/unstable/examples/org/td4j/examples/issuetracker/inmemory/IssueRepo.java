package org.td4j.examples.issuetracker.inmemory;

import org.td4j.core.reflect.Operation;
import org.td4j.core.tk.ObjectTK;

public class IssueRepo {
	
	private final EntityRepo repo;
	
	public IssueRepo(EntityRepo repo) {
		this.repo = ObjectTK.enforceNotNull(repo, "repo");
	}
	
	@Operation
	public Issue createIssue(IssueContainer context) {
		final Issue issue = new Issue(context);
		
		context.addIssue(issue);
		
		repo.put(Issue.class, issue);
		return issue;
	}

}
