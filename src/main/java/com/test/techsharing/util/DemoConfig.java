package com.test.techsharing.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DemoConfig {

    private DemoConfig() {
    }

    public static Properties load() {
        Properties properties = new Properties();
        try (InputStream inputStream = DemoConfig.class.getClassLoader().getResourceAsStream("demo.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("demo.properties not found on classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load demo.properties", e);
        }

        System.getProperties().forEach((key, value) -> {
            if (key instanceof String && ((String) key).startsWith("demo.")) {
                properties.setProperty((String) key, String.valueOf(value));
            }
        });
        return properties;
    }
}
