package org.td4j.examples.tagger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.td4j.core.env.DefaultSvcRepository;
import org.td4j.core.env.SvcRepo;
import org.td4j.core.env.SvcRepository;
import org.td4j.swing.workbench.Workbench;

public class AppLauncher {
	
	public static void main(String[] args) {
		final Setup setup = new Setup();
		final Object initialNavigation = setup.run();		
		
		// TODO inject svcRepo to workbench
		System.out.println(setup.repo);
		
		Workbench.start(initialNavigation, TagGroup.class, Tag.class, Resource.class);
	}
	
	
	private static class Setup {
		
		private final SvcRepository repo = new DefaultSvcRepository();
		
		private Object run() {
			
			// TODO pass SvcRepo as parameter to Workbench
			SvcRepo.init(repo);
			
			initCompanions(repo);
			
			// TODO return initial navigation -> TagGroupsCO.findAll
			return null;
		}		
		
		private void initCompanions(SvcRepository repo) {
			final EntityManager em = initEntityManager();
			
			repo.setSingletonService(TagGroupCO.class, new TagGroupCO(em));
		}		
		
		private EntityManager initEntityManager() {
			final EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("tagger");
			final EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
			    public void run() {
			    	em.getTransaction().commit();
			    	em.close();
			    	emf.close();		    	
			    }
			});	
			
			return em;
		}
	}
	
}
