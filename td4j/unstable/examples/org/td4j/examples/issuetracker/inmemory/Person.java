package org.td4j.examples.issuetracker.inmemory;

import java.util.ArrayList;
import java.util.List;

public class Person extends NamedElement {
	
	private List<Issue> reportedIssues = new ArrayList<Issue>();	
	private List<Issue> assignedIssues = new ArrayList<Issue>();

}
