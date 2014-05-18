package org.superbiz.cdi.properties;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDbFactory {

    MongoClient mongo;

    @Inject
    @ConfigProperty(name = "mongodb.host") // <1> Using +ConfigProperty+ annotation from _DeltaSpike_ to inject the configuration value.
    String host;

    @Inject
    @ConfigProperty(name = "mongodb.port")
    Integer port;

    @PostConstruct // <2> After the construction of this POJO, the +MongoClient+ instance is created with injected parameters.
    public void init() throws UnknownHostException {
        mongo = new MongoClient(host, port);
    }

    @Produces
    @RecipesCollection
    public DBCollection getRecipeCollection() { // <3> This method acts as a producer for _recipies_ _MongoDB_ collections. 
        DB db = mongo.getDB("test");
        return db.getCollection("recipes");
    }

}
