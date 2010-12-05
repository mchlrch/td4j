package org.td4j.examples.tagger;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JpaTK {

	public static <T> List<T> findAll(EntityManager em, String queryString) {
		final Query query = em.createQuery(queryString);		
		return findAll(query);
	}
	
	public static <T> List<T> findAll(Query query) {
		final List<T> result = query.getResultList();		
		return result;
	}	
	
	public static <T> T findFirst(EntityManager em, String queryString) {
		final Query query = em.createQuery(queryString);
		return findFirst(query);
	}	
	
	public static <T> T findFirst(Query query) {
		final List<T> result = query.getResultList();		
		return  ! result.isEmpty() ? result.get(0) : null;		
	}
	
	public static int count(EntityManager em, String queryString) {
		final Query query = em.createQuery(queryString);
		return count(query);
	}
	
	public static int count(Query query) {
		return query.getResultList().size();
	}
}
