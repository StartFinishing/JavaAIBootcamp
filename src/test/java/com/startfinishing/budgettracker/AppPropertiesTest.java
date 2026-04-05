package com.startfinishing.budgettracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * {@link AppProperties} loads once when the class is first used. Gradle sets {@code APP_ENV=test}
 * for the test JVM (see {@code build.gradle}), so {@code application-test.properties} from {@code
 * src/main/resources} is loaded.
 *
 * <p>Do not add an empty {@code src/test/resources/application-test.properties}: it appears earlier
 * on the test classpath and would shadow the main file, leaving {@link AppProperties} with no keys.
 */
class AppPropertiesTest {

  @Test
  void getPropertyReturnsValueFromApplicationTestProperties() {
    assertEquals("memory", AppProperties.getProperty("app.storage"));
    assertEquals("./PersistedStorage/junit.csv", AppProperties.getProperty("app.storage.csv.path"));
    assertEquals("test", AppProperties.getProperty("app.profile"));
  }

  @Test
  void getPropertyReturnsNullWhenKeyIsMissing() {
    assertNull(AppProperties.getProperty("no.such.property.key"));
  }
}
