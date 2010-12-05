package org.td4j.examples.tagger;

import java.util.List;

import javax.persistence.EntityManager;

import org.td4j.core.reflect.Operation;

import ch.miranet.commons.ObjectTK;

public class TagGroupCO {
	
	private final EntityManager em;
	
	public TagGroupCO(EntityManager em) {
		this.em = ObjectTK.enforceNotNull(em, "em");
	}
	
	@Operation
	public TagGroup create(String name) {
		if (findByName(name) != null) throw new IllegalArgumentException("duplicate TagGroup: " + name);
		
		final TagGroup group = new TagGroup(name);		
		em.persist(group);
		
		return group;
	}

	@Operation
	public List<TagGroup> findAll() {
		return JpaTK.findAll(em, "SELECT tg FROM TagGroup tg ORDER BY tg.name");
	}
	
	public TagGroup findByName(String name) {
		return JpaTK.findFirst(em, "SELECT tg FROM TagGroup tg WHERE tg.name = '" + name + "'");
	}
	
}
