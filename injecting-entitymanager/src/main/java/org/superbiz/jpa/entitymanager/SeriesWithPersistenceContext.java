package org.superbiz.jpa.entitymanager;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SeriesWithPersistenceContext {

	@PersistenceContext
	private EntityManager entityManager;
	
	public void createSerie(Serie serie) {
		this.entityManager.persist(serie);
	}
	
}
