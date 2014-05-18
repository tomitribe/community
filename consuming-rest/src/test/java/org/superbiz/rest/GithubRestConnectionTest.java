package org.superbiz.rest;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EjbJarDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.superbiz.rest.GithubRestConnection;

/**
 * ****
 * After reading this example, you'll be able to:
 * 
 * * inject parameters to an EJB using +@Resource+.
 * * write a client that consumes a _Restful webservice_ using _Apache TomEE_.
 * * create +ejb-jar.xml+ files.
 * * test applications that consumes _Restful webservices_ using _Arquillian_.
 * ****
 * 
 * When dealing _Restful web services_ your application may implement one or both of next roles. The first one a *producer* of _REST_ content. The second one a *consumer* of _REST_ content from another producer (internal or external).
 * 
 * In case of a *producer* you will create _POJOs_ or _EJBs_ and annotate them with +@Path+, +GET+, +POST+, ... and for testing services you can use _Arquillian_ + _REST-assured_ but this is another history.   
 * 
 * In case of a *consumer* you can use _jax-rs2.0_ client approach or use any framework like _HttpClient_, _Retrofit_, _Apache CXF Client_, ... and for testing will depend on the environment.
 * Let's suppose you have written a client _API_ that connects to _github Restful services_ to get all issues from a project.
 * In case of _test_ environment probably you will use some kind of stub server running on _localhost_, but in _pre_ environment you will use an external (real) host. In case of github https://api.github.com[Github API].
 * 
 * So we need a way to inject the host depending on the environment where tests are being run. If your client is implemented as a _Session Bean_ you can use the +ejb-jar.xml+ file to inject it. Let's see an example of how to do it.
 * 
 * 
 * 
 * 
 * 
 */
@RunWith(Arquillian.class)
public class GithubRestConnectionTest {

	@Deployment static JavaArchive createDeployment() {
		
		JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
				.addClass(GithubRestConnection.class)
				.addAsManifestResource(new StringAsset(descriptor().exportAsString()), "ejb-jar.xml") // <1> +ejb-jar.xml+ file must be at _Manifest_ folder. We use _ShrinkWrap Descriptors_ to generate an _ejb-jar_ file.
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
				
				return javaArchive;
		
	}
	
	public static  EjbJarDescriptor descriptor() {
	    return Descriptors.create(EjbJarDescriptor.class)
	    		.getOrCreateEnterpriseBeans()
				.getOrCreateSession()
				.ejbName("GithubRestConnection") // <2> The +ejbName+ field should be the name of the session bean.
				.createEnvEntry()
					.envEntryName("org.superbiz.rest.GithubRestConnection/url") // <3> In case you use +resource+ annotation without a name the whole classname and field name separated by slash '/' should be provided.
					.envEntryType("java.lang.String") // <4> The type of the attribute.
					.envEntryValue("http://localhost:8080") // <5> The value of the attribute, that in this case because of test environment, _localhost_ is used. 
					.up()
				.up().up();
	                
	}
	
	@EJB
	GithubRestConnection restConnection;
	
	@Test
	public void execute() {
		
		restConnection.getData();
		
	}
	
}
