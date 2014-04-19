package org.superbiz.jpa.entitymanager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;


@Stateless
public class SeriesWithInjection {

	@Inject              // <1> +@PersistenceContext+ is not required anymore. +@Inject+ can be used without any problem.
	@SeriesEntityManager // <2> We add some semantic meaning to the injection and also helps to distinguish in case of different _persistence units_.
	private EntityManager entityManager;
	
	public void createSerie(Serie serie) {
		this.entityManager.persist(serie);
	}
	
}
