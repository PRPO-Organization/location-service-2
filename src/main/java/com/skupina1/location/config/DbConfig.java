package com.skupina1.location.config;


import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
//@DataSourceDefinition(
//        name = "java:app/jdbc/ExampleDataSource",
//        className = "org.postgresql.ds.PGSimpleDataSource",
//        user = "location-user",
//        password = "password123",
//        databaseName = "locations",
//        serverName = "postgis",
//        portNumber = 5432
//)
public class DbConfig {
    // This class only declares the datasource
}
