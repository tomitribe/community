package org.superbiz.cdi.properties;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@ApplicationScoped
public class MongoDbFactory {

    MongoClient mongo;

    @Inject
    @ConfigProperty(name = "host") // <1> Using +Inject+ and +ConfigProperty+ annotation from _DeltaSpike_ together to inject the configuration value.
    private String host;

    @Inject
    @ConfigProperty(name = "port")
    private Integer port; // <2> Native conversions are provided automatically.

    @PostConstruct // <3> After the construction of this POJO, the +MongoClient+ instance is created with injected parameters.
    public void init() {
        try {
            mongo = new MongoClient(host, port);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Produces
    @RecipesCollection
    public DBCollection getRecipeCollection() { // <4> This method acts as a producer for _recipies MongoDB_ collections. 
        DB db = mongo.getDB("test");
        return db.getCollection("recipes");
    }
    
    public String getHost() {
        return host;
    }
    
    public Integer getPort() {
        return port;
    }

}
