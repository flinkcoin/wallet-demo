/*
 * Copyright © 2021 Flink Foundation (info@flinkcoin.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flinkcoin.wallet.main.configuration;

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
