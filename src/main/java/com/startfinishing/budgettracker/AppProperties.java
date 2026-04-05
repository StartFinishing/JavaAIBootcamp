package com.startfinishing.budgettracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppProperties {

  private static final Properties properties = new Properties();
  private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);

  static {
    String resourceName;
    String env = System.getProperty("APP_ENV");
    if (env == null || env.isBlank()) {
      logger.warn("System property APP_ENV is not set, using default environment: dev");
      env = "dev";
    }
    logger.debug("Environment: " + env);
    switch (env) {
      case "dev":
        logger.info("Loading development environment properties");
        resourceName = "application-development.properties";
        break;
      case "prod":
        logger.info("Loading production environment properties");
        resourceName = "application-production.properties";
        break;
      case "test":
        logger.info("Loading test environment properties");
        resourceName = "application-test.properties";
        break;
      default:
        logger.info("Invalid APP_ENV value '{}'; expected dev, prod, or test", env);
        throw new RuntimeException("Invalid APP_ENV value: " + env);
    }

    logger.debug("Loading properties from classpath: {}", resourceName);

    try (InputStream input =
        AppProperties.class.getClassLoader().getResourceAsStream(resourceName)) {
      if (input == null) {
        throw new IllegalStateException(
            "Classpath resource not found: "
                + resourceName
                + " (place it under src/main/resources/)");
      }
      properties.load(input);
    } catch (IOException e) {
      logger.error("Failed to load {}", resourceName, e);
      throw new RuntimeException("Failed to load " + resourceName, e);
    }
  }

  public static String getProperty(String key) {
    String value = properties.getProperty(key);
    if (value == null) {
      logger.error("Property " + key + " not found");
    }
    return value;
  }
}
