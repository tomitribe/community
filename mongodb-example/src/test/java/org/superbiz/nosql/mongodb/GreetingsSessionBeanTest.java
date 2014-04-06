package org.superbiz.nosql.mongodb;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;

import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * = Apache TomEE and custom Resources 
 *
 * In this sample we're going to explore how to create a custom resource in +Apache TomEE+ for +MongoDB+ so we can use
 * +@Resource+ annotation as we usually do for +DataSources+. Of course the same approach can be used for any other
 * resource.
 * 
 * Let's create a simple +Stateless Session+ Bean that will deal with +com.mongodb.MongoClient+ connection class.
 * 
 * include::src/main/java/org/superbiz/nosql/mongodb/GreetingsSessionBean.java[]
 * 
 * As you can see +@Resource+ annotation is used over +com.mongodb.MongoClient+ class, +Apache TomEE+ will be the
 * responsible of creating and injecting the resource.
 * 
 * Next step is configuring the +MongoDB+ connection. To do it we only need to create a +resources.xml+ file with
 * required parameters.
 * 
 * include::src/test/resources/resources.xml
 * 
 * Note that file is self-explained. +MongoClient+ has a constructor with single parameter where you set the host and in
 * this case this is the constructor that is resolved. You can think about +resources.xml+ file as +Spring+-like file
 * where constructors and getter/setters are resolved.
 * 
 * To be processed +resources.xml+ file should be added inside +WEB-INF+ directory.
 * 
 * And finally an +Arquillian+ test to check that resource is created correctly.
 * 
 * First step is creating a deployment file:
 * 
 * include::src/test/java/org/superbiz/nosql/mongodb/GreetingsSessionBeanTest#deploy().java[]
 * 
 * Thanks of +ShrinkWrap+, we can create as many +resources.xml+ files depending on the environments you must deploy the
 * application and add them selectively.
 * 
 * TIP: you can even create one +resources.xml+ file and override their values using system properties instead of having
 * several files. For example in this example we could override address property by using _-DmongoClient.address=ip_.
 * 
 * And finally we can call our +EJB+ as we usually do, and +Apache TomEE+ will take care for us of injecting
 * +MongoClient+ class.
 * 
 */
@RunWith(Arquillian.class)
public class GreetingsSessionBeanTest {

    @Deployment
    public static WebArchive deploy() {

        WebArchive webArchive = ShrinkWrap
                .create(WebArchive.class)
                .addClass(Greetings.class)
                .addClass(GreetingsSessionBean.class)
                .addAsWebInfResource("resources.xml")   // <1> resources.xml is added from classpath to WEB-INF
                .addAsLibraries(
                        Maven.resolver().loadPomFromFile("pom.xml").resolve("org.mongodb:mongo-java-driver")
                                .withTransitivity().asFile());

        return webArchive;

    }

    @EJB
    GreetingsSessionBean greetingsSessionBean;

    /**
     * A MongoDB server must be started at _localhost_ manually, but it is not required to create any _db_ nor _collection_
     * because they are automatically created by the driver.
     * 
     * include::src/test/java/org/superbiz/nosql/mongodb/GreetingsSessionBeanTest#shouldCreateGreetingsOnMongoDB().java[]
     */
    @Test
    public void shouldCreateGreetingsOnMongoDB() {

        greetingsSessionBean.removeGreetings();                             // <1> Previous Greetings are removed

        greetingsSessionBean.createPerson(new Greetings("Hello World!!"));  // <2> Create a new Greeting

        List<Greetings> greetings = greetingsSessionBean.getGreetings();    // <3> We assert that the Greeting is inserted
        assertThat(greetings, hasItem(new Greetings("Hello World!!")));

    }

}
