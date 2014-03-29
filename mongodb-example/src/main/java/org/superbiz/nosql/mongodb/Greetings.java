package org.superbiz.nosql.mongodb;

import javax.enterprise.inject.Model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Model
public class Greetings {

    private String message;

    public Greetings() {
        super();
    }

    public Greetings(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BasicDBObject toDBObject() {
        BasicDBObject doc = new BasicDBObject();

        doc.put("message", this.message);

        return doc;
    }

    public static Greetings fromDBObject(DBObject doc) {
        Greetings greetings = new Greetings();

        greetings.message = (String) doc.get("message");
        return greetings;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Greetings other = (Greetings) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.message;
    }

}
