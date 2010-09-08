package org.td4j.examples.issuetracker.jpa;

import javax.persistence.EntityManager;

import org.td4j.core.reflect.Operation;

public class IssueRepo extends EntityRepo {
	
	public IssueRepo(EntityManager em) {
		super(em);
	}
	
	@Operation
	public Issue createIssue(Context context) {
		final Issue issue = new Issue(context);
		em.persist(issue);
		return issue;
	}

}
