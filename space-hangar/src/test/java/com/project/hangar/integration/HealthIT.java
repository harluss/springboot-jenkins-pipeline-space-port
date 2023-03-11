package com.project.hangar.integration;

import com.project.hangar.common.BaseIT;
import com.project.hangar.integration.steps.HealthSteps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.annotation.PostConstruct;

@ExtendWith({SerenityJUnit5Extension.class})
class HealthIT extends BaseIT {

  @Steps
  private HealthSteps healthSteps;

  @LocalServerPort
  private int port;

  @PostConstruct
  public void setDefaultPort() {
    SerenityRest.setDefaultPort(port);
  }

  @SuppressWarnings("java:S2699")
  @DisplayName("Health Check")
  @Test
  void healthCheck() {
    healthSteps.givenAppIsRunning();
    healthSteps.whenGetRequestIsSentToHealthEndpoint();
    healthSteps.thenResponseShouldBe200AndContainStatusUp();
  }

}
