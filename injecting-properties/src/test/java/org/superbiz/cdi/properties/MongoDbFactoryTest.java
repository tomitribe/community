package org.superbiz.cdi.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * = Injecting Properties
 * 
 * .Overview
 * ****
 * After reading this example, you'll be able to:
 * 
 * * Inject values from properties file into simple POJO using _DeltaSpike_.
 * * Use _MongoDB java driver_ inside _Java EE_ application.
 * * Write tests using _Arquillian_. 
 * ****
 * 
 * One of the great improvement in _Java EE 5_ and beyond it is the introduction of _CDI_ (Context and Dependency Injection).
 * _CDI_ is used for injecting dependencies among a lot of other things like events, interceptors, ... and can be used in _POJOs_.
 *   
 * In some cases instead of injecting other objects (as a dependency injection), you want to inject a value from a properties file into a class that needs to be configured externally. 
 * 
 * In current example you are going to explore how to configure a _MongoDB_ client, setting required parameters, that is _host_ and _port_, from a properties file called +mongodb.properties+.
 * 
 * The easiest way to do is using _DeltaSpike_ project.
 * 
 * _DeltaSpike_ consists on a number of portable _CDI_ extensions that provide useful features for Java application developers. 
 * And in this case a core feature which is the configuration mechanism is going to be used.     
 * 
 * The mechanism used by _DeltaSpike_ allows developers for dynamic configuration in case of JAR drop-in.
 * 
 * The first thing is add _DeltaSpike_ dependencies:
 * 
 * include::pom.xml[xmlns:mvn=http://maven.apache.org/POM/4.0.0 /mvn:project/mvn:dependencies/mvn:dependency[mvn:groupId[contains(., 'org.apache.deltaspike.core')]]]
 * 
 * Then you define our service to return the location of configuration file based on classpath. And this is done by implementing an SPI interface called +PropertyFileConfig+. 
 * 
 * include::src/main/java/org/superbiz/cdi/properties/MongoDbConfigurationFile.java[]
 * 
 * Next step is to register this class as a service. To do this, a file called +org.apache.deltaspike.core.api.config.PropertyFileConfig+ inside +META-INF/services+ is created:
 * 
 * .META-INF/services/org.apache.deltaspike.core.api.config.PropertyFileConfig
 * [source]
 * ----
 * org.superbiz.cdi.properties.MongoDbConfigurationFile
 * ----
 * 
 * And that's all, now you can use +ConfigProperty+ annotation with +@Inject+ in your _POJOs_ and _CDI_ container will inject values configured within properties file.
 * In your case a file called _mongodb.properties_ located on root classpath.
 * 
 * Let's see an example of +mongodb.properties+ file.
 * 
 * .mongodb.properties
 * [source]
 * ----
 * host = locahost
 * port = 27017
 * ----
 * 
 * And then your _POJO_ may look like:
 * 
 * include::src/main/java/org/superbiz/cdi/properties/MongoDbFactory.java[]
 * 
 * To test it an _Arquillian_ test is going to be used. For this case,  +JavaArchive+ must contain not only our code but also the _DeltaSpike_ library.
 * 
 * include::src/test/java/org/superbiz/cdi/properties/MongoDbFactoryTest.java[]
 * 
 * '''
 * 
 * Injecting values from external file using _Delta Spike_ it is easy, but it has the drawback of having to add _Delta Spike_ as dependency. Probably this will be fixed in next version of _CDI_ spec.
 * 
 * If you are using +EJBs+ you can use +ejb-jar.xml+ for that purpose, but this is another history.
 * 
 * And you can even create your own +@Resource+ provider in _Apache TomEE_ as explained in https://github.com/tomitribe/community/tree/master/mongodb-example
 * 
 * WARNING: Creating your own resource provider will make your application not being portable across application servers.
 * 
 */
@RunWith(Arquillian.class)
public class MongoDbFactoryTest {

    private static final String MONGODB_PROPERTIES = "host = localhost" + System.getProperty("line.separator") + 
    		"port = 27017";
    
    @Deployment @OverProtocol("Servlet 3.0")
    public static JavaArchive createDeployment() {
        
        JavaArchive application = ShrinkWrap.create(JavaArchive.class) // <1> A +JavaArchive+ is created with required classes bundled.
                         .addPackage(MongoDbFactory.class.getPackage())
                         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                         .addAsServiceProvider(PropertyFileConfig.class, MongoDbConfigurationFile.class) // <2> +PropertyFileConfig+ service is registered in +META-INF/services+ with proper content.
                         .addAsResource(new StringAsset(MONGODB_PROPERTIES), "mongodb.properties"); // <3> +mongodb.properties+ is created from +String+ for testing purposes.
        
        application =  merge(application, Maven.resolver().loadPomFromFile("pom.xml")
                                        .resolve("org.apache.deltaspike.core:deltaspike-core-impl")
                                        .withTransitivity().as(JavaArchive.class)); // <4> _DeltaSpike_ dependencies are merged inside the +JavaArchive+.
        
        return application;
        
    }
    
    private static JavaArchive merge(JavaArchive originalArchive, JavaArchive... mergedJavaArchives) {
        for (JavaArchive javaArchive : mergedJavaArchives) {
            originalArchive.merge(javaArchive);
        }
        
        return originalArchive;
        
    }
    
    @Inject
    MongoDbFactory mongoDbFactory;
    
    @Test
    public void should_load_mongodb__configuration_from_properties_file() {
       
       assertThat(mongoDbFactory.getHost(), is("localhost"));
       assertThat(mongoDbFactory.getPort(), is(27017));
        
    }
    
}
