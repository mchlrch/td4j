package org.td4j.examples.issuetracker.jpa;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Person extends NamedElement {
	
	@OneToMany(mappedBy="reportedBy")
	private List<Issue> reportedIssues;
	
	@OneToMany(mappedBy="assignedTo")
	private List<Issue> assignedIssues;

}
