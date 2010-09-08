package org.td4j.examples.issuetracker.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedElement {
	
	@SuppressWarnings("unused") @Id @GeneratedValue private Integer id;
	
	private String name;
	
	public String getName()            { return name; }
	public void setName(String name)   { this.name = name; }
	
	@Override	public String toString() { return name; }

}
