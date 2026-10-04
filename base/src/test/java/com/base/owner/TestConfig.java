package com.base.owner;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:test.properties"
})
@Config.LoadPolicy(Config.LoadType.MERGE)
public interface TestConfig extends Config {

    @Key("baseUrl")
    String baseUrl();

    @Key("isDocker")
    boolean isDocker();

    @Key("threadCount")
    int threadCount();

    @Key("browser")
    String browser();
}
