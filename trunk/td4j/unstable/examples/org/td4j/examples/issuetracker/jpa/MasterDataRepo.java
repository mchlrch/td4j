package org.td4j.examples.issuetracker.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.td4j.core.reflect.Operation;

public class MasterDataRepo extends EntityRepo {
	
	public MasterDataRepo(EntityManager em) {
		super(em);
	}
	
	@Operation
	public Context createContext(IssueTemplate template, String name) {
		final Context ctx = new Context(name, template);
		em.persist(ctx);
		return ctx;
	}
	
	@Operation
	public IssueTemplate createTemplate(String name) {
		final IssueTemplate template = new IssueTemplate();
		template.setName(name);
		em.persist(template);
		return template;
	}
	
	@Operation
	public Severity createSeverity(IssueTemplate template, String name) {
		final Severity severity = new Severity();
		severity.setTemplate(template);
		severity.setName(name);
		em.persist(severity);
		return severity;
	}
		
	@Operation
	public Status createStatus(IssueTemplate template, String name, boolean resolved) {
		final Status status = new Status();
		status.setTemplate(template);
		status.setName(name);
		status.setResolved(resolved);
		em.persist(status);
		return status;
	}
	
	@Operation
	public Module createModule(IssueTemplate template, String name) {
		final Module module = new Module();
		module.setTemplate(template);
		module.setName(name);
		em.persist(module);
		return module;
	}
	
	@Operation
	public List<IssueTemplate> findAllTemplates() {
		return em.createQuery("SELECT t FROM IssueTemplate AS t").getResultList();
	}
	
	@Operation
	public List<Context> findAllContexts() {
		return em.createQuery("SELECT ctx FROM Context AS ctx").getResultList();
	}

}
