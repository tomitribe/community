package org.superbiz.nosql.mongodb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Named;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@Named
@Stateless
public class GreetingsSessionBean {

	@Resource(name = "mongoClient")
	private MongoClient mongoClient;

	DBCollection greetingsCollection;

	@PostConstruct
	private void initDB() {
		DB db = mongoClient.getDB("greetingsDB");
		greetingsCollection = db.getCollection("greetings");
		greetingsCollection.remove(new BasicDBObject());
	}

	public void createPerson(Greetings greetings) {
		BasicDBObject doc = greetings.toDBObject();
		greetingsCollection.insert(doc);
	}

	public List<Greetings> getGreetings() {
		List<Greetings> greetings = new ArrayList<>();
		
		DBCursor cur = greetingsCollection.find();
		
		for (DBObject dbo : cur.toArray()) {
			greetings.add(Greetings.fromDBObject(dbo));
		}

		return greetings;
	}

	public void removeGreetings() {
	    greetingsCollection.remove(new BasicDBObject());
	}
	
}
