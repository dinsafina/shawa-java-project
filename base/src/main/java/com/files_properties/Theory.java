package com.files_properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Theory {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();

        try(InputStream resourceAsStream = Theory.class.getClassLoader().getResourceAsStream("app.properties")) {
            properties.load(resourceAsStream);
        }

        String url = properties.getProperty("url");
        int threadCount = Integer.parseInt(properties.getProperty("threadCount"));
        boolean isRemote = Boolean.parseBoolean(properties.getProperty("isRemote"));

        Map<String, String> getEnv = System.getenv();


    }
}
