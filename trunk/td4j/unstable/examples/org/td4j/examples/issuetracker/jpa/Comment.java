package org.td4j.examples.issuetracker.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Comment {
	
	@SuppressWarnings("unused") @Id @GeneratedValue private Integer id;
	
	@ManyToOne private Issue issue;
	@ManyToOne private Person creator;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Link link;

}
