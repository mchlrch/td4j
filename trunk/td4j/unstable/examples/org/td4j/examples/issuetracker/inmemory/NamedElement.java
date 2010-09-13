package org.td4j.examples.issuetracker.inmemory;

abstract class NamedElement {	
	
	private String name;
	
	public String getName()            { return name; }
	public void setName(String name)   { this.name = name; }
	
	@Override	public String toString() { return name; }

}
