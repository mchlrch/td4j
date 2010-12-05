package org.td4j.examples.issuetracker.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Link {
	
	@SuppressWarnings("unused") @Id @GeneratedValue private Integer id;
	
	private Comment comment;

}
