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

@RunWith(Arquillian.class)
public class GreetingsSessionBeanTest {

    @Deployment
    public static WebArchive deploy() {
        
        WebArchive webArchive = ShrinkWrap
                .create(WebArchive.class)
                .addClass(Greetings.class)
                .addClass(GreetingsSessionBean.class)
                .addAsWebInfResource("resources.xml")
                .addAsLibraries(
                        Maven.resolver().loadPomFromFile("pom.xml").resolve("org.mongodb:mongo-java-driver")
                                .withTransitivity().asFile());
        
        return webArchive;
        
    }

    @EJB
    GreetingsSessionBean greetingsSessionBean;
    
    @Test
    public void shouldCreateGreetingsOnMongoDB() {
        
        greetingsSessionBean.removeGreetings();
        
        greetingsSessionBean.createPerson(new Greetings("Hello World!!"));
        
        List<Greetings> greetings = greetingsSessionBean.getGreetings();
        assertThat(greetings, hasItem(new Greetings("Hello World!!")));
        
    }
    
}
