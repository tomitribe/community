package org.superbiz.jpa.entitymanager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProducer {


	@Produces @SeriesEntityManager // <1> An entity manager is produced and will be injected with +@SeriesEntityManager+ qualifier. 
	@PersistenceContext // <2> Because there is only one persistence unit, there is no need to use +unitName+ attribute.
    EntityManager em;
    
}
