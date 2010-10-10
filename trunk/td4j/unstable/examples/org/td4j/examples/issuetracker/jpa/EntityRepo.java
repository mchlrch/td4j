package org.td4j.examples.issuetracker.jpa;

import javax.persistence.EntityManager;

public class EntityRepo {
	
	protected final EntityManager em;
	
	public EntityRepo(EntityManager em) {
		this.em = em;
	}

}
