package org.superbiz.cdi.properties;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

public class MongoDbConfigurationFile implements PropertyFileConfig {

    private static final long serialVersionUID = -7762380573887097591L;

    @Override
    public String getPropertyFileName() {
        return "mongodb.properties"; // <1> This method should return the location and name of the properties file to be loaded.
    }

}