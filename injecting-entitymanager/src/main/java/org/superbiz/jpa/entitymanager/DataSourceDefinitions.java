package org.superbiz.jpa.entitymanager;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@javax.annotation.sql.DataSourceDefinitions({
    @DataSourceDefinition (
        className="org.hsqldb.jdbcDriver",
        name="series",
        user="sa",
        password="",
        databaseName="seriesdb",
        properties = {"connectionAttributes=;create=true"},
        url = "jdbc:hsqldb:mem:seriesdb"
    ),
    @DataSourceDefinition (
            transactional = false,
            className="org.hsqldb.jdbcDriver",
            name="seriesUnmanaged",
            user="sa",
            password="",
            databaseName="seriesdb",
            properties = {"connectionAttributes=;create=true"},
            url = "jdbc:hsqldb:mem:seriesdb"
        )
    })
public class DataSourceDefinitions {

}
