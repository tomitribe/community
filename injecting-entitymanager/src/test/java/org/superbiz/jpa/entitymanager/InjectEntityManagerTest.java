package org.superbiz.jpa.entitymanager;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence20.PersistenceDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.superbiz.jpa.entitymanager.DataSourceDefinitions;
import org.superbiz.jpa.entitymanager.EntityManagerProducer;
import org.superbiz.jpa.entitymanager.Serie;
import org.superbiz.jpa.entitymanager.SeriesEntityManager;
import org.superbiz.jpa.entitymanager.SeriesWithInjection;

/**
 * = Injecting EntityManager
 * 
 * .Overview
 * ****
 * After reading this example, you'll be able to:
 * 
 * * Understand the advantages of using +@Inject+ instead of +@PersistenceContext+.
 * * Use _JPA_ and _Apache TomEE_.
 * * Write Persistence tests using _Arquillian_. 
 * ****
 * 
 * Almost all projects you develop with Java EE will contain a persistence layer and probably you will use +JPA+ for dealing with +SQL+ systems.
 * 
 * The main class for using +JPA+ is +EntityManager+. It is the facade class which offers operations to deal with a specific _persistence unit_, it is associated to a _persistence context_ and manage the lifecycle of entity instances.
 * 
 * One way to get an +EntityManager+ is using +@PersistenceContext+.
 * 
 * We are going to use an example of an application that stores information about tv series.
 * 
 * We are going to use +javax.annotation.sql.DataSourceDefinition+ annotation to define the datasource.
 * 
 *  include::src/main/java/org/superbiz/jpa/entitymanager/DataSourceDefinitions.java[]
 * 
 * NOTE: _Apache TomEE_ comes with _HSQLDB_ already installed so you don't have to add it as dependency in your project.
 * 
 * Then let's take a look to the class that is going to use to create a new series using +@PersistenceContext+.
 * 
 * include::src/main/java/org/superbiz/jpa/entitymanager/SeriesWithPersistenceContext.java[]
 *
 * When there is only one _persistence unit_, +PersistenceContext+ annotation can be used without any parameter.
 * But what's happen if some day you add a new _persistence unit_? You will need to use +unitName+ attribute of +PersistenceContext+ annotation to set _persistence unit_ name to the +EntityManager+. 
 * And this is a problem because you will need to modify all places where +PersistenceContext+ is used and set unit name.
 * 
 * To avoid this problem and implement a clean way to get an +EntityManager+, let's use +CDI+.
 * 
 * First thing we are going to create a qualifier annotation to add sense to the +EntityManager+ that is going to be injected, so you know exactly which _persistence unit_ is being used.
 * 
 * include::src/main/java/org/superbiz/jpa/entitymanager/SeriesEntityManager.java[]
 *
 * And then a producer class. This class will produce all _entity managers_ required by the application, in this case only one.
 * 
 * include::src/main/java/org/superbiz/jpa/entitymanager/EntityManagerProducer.java[]
 *
 * And finally we can inject an _entity manager_ as any normal bean. 
 * 
 * include::src/main/java/org/superbiz/jpa/entitymanager/SeriesWithInjection.java[]
 * 
 * Notice that now if a new _persistence unit_ is added you only have to modify the +EntityManagerProducer+ class by using +unitName+ and the whole application will continue working.
 * 
 * Moreover adding a new _entity manager_ will not conflict the state of current application because _entity manager_ is qualified, so you know exactly every time which one is used.
 *
 */
@RunWith(Arquillian.class)
public class InjectEntityManagerTest {

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
				.addClass(DataSourceDefinitions.class)
				.addClass(Serie.class)
				.addClass(SeriesWithInjection.class)
				.addClass(SeriesEntityManager.class)
				.addClass(EntityManagerProducer.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource(new StringAsset(descriptor().exportAsString()), "persistence.xml");
		return javaArchive;
	}
	
	public static  PersistenceDescriptor descriptor() {
	    return Descriptors.create(PersistenceDescriptor.class)
	                .createPersistenceUnit()
	                    .name("series")
	                    .getOrCreateProperties()
	                        .createProperty()
	                            .name("openjpa.jdbc.SynchronizeMappings")
	                            .value("buildSchema(ForeignKeys=true)")
	                        .up()
	                     .up()
	                .jtaDataSource("series")
	                .clazz("org.superbiz.jpa.entitymanager.Serie")
	                .up();
	}
	
	@EJB
	SeriesWithInjection seriesWithInjection;
	
	@Test
	public void shouldInsertSerieInsideDatabase() {
		
		Serie dexter = new Serie("Showtime", "Dexter", "Serial drama");
		seriesWithInjection.createSerie(dexter);
		
		assertThat(dexter.getId(), is(notNullValue()));
		
	}
	
}
