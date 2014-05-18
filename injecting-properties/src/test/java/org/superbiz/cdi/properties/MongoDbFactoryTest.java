package org.superbiz.cdi.properties;

import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;
import org.jboss.arquillian.container.test.api.Deployment;
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
 * * How to inject values from properties file into simple POJO using _DeltaSpike_.
 * * How to use _MongoDB_ inside _Java EE_ application.
 * * Write tests using _Arquillian_. 
 * ****
 * 
 * One of the great improvement in _Java EE 5_ and beyond it is the introduction of _CDI_ (Context and Dependency Injection).
 * _CDI_ is used for injecting dependencies among a lot of other things like events, interceptors, ... and can be used in simple _POJOs_.
 *   
 * In some cases instead of injecting other objects you want to inject a value from a properties file. The easiest way to do is using _DeltaSpike_ project.
 * 
 * _DeltaSpike_ consist of a number of portable _CDI_ extensions that provide useful features for Java application developers. 
 * And in this case we are going to use one core feature which can be used as configuration mechanism.     
 * 
 * The mechanism used by _DeltaSpike_ allows developers for dynamic configuration in case of JAR drop-in.
 * 
 * The first thing to do is define our service to return the configuration file location. And this is done by implementing the SPI interface called ++. 
 * 
 * include::src/main/java/org/superbiz/cdi/properties/MongoDbConfigurationFile.java[]
 * 
 */
@RunWith(Arquillian.class)
public class MongoDbFactoryTest {

    private static final String MONGODB_PROPERTIES = "mongodb.host = localhost\n" + 
    		"mongodb.port = 27017";
    
    @Deployment
    public static JavaArchive createDeployment() {
        
        JavaArchive application = ShrinkWrap.create(JavaArchive.class) // <1> A +JavaArchive+ is created with required classes bundled.
                         .addPackage(MongoDbFactory.class.getPackage())
                         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                         .addAsServiceProvider(PropertyFileConfig.class, MongoDbConfigurationFile.class) // <2> +PropertyFileConfig+ service is registered in +META-INF/services+.
                         .addAsResource(new StringAsset(MONGODB_PROPERTIES), "mongodb.properties"); // <3> +mongodb.properties+ is created from +String+ for testing purposes.
        
        return merge(application, Maven.resolver().loadPomFromFile("pom.xml")
                                        .resolve("org.apache.deltaspike.core:deltaspike-core-impl")
                                        .withTransitivity().as(JavaArchive.class)); // <4> _DeltaSpike_ dependencies are merged inside the +JavaArchive+.
        
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
        
        
        
    }
    
}
