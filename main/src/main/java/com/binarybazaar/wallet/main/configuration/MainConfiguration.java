package com.binarybazaar.wallet.main.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({
    "file:wallet.configuration"
})
public interface MainConfiguration extends Config {

    @Key("server.http.port")
    @DefaultValue("8383")
    int port();

    @Key("server.http.logging")
    @DefaultValue("true")
    boolean httpLogging();

    @Key("client.api.port")
    @DefaultValue("8081")
    int apiPort();

    @Key("webapp.basepath")
    @DefaultValue("/")
    String basePath();

    @Key("database.server")
    @DefaultValue("localhost")
    String serverName();

    @Key("database.port")
    @DefaultValue("5432")
    int portNumber();

    @Key("database.name")
    @DefaultValue("main")
    String databaseName();

    @Key("database.username")
    @DefaultValue("postgres")
    String username();

    @Key("database.password")
    @DefaultValue("postgres")
    String password();

}
