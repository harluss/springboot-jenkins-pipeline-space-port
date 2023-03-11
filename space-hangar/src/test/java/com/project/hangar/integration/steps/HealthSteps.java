package com.project.hangar.integration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.then;
import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class HealthSteps {

  private static final String TEST_ACTUATOR_API = "/actuator/health";

  @Given("app is running")
  public void givenAppIsRunning() {
    given();
  }

  @When(" GET request is sent to Health endpoint")
  public void whenGetRequestIsSentToHealthEndpoint() {
    when()
        .get(TEST_ACTUATOR_API);
  }

  @Then("response should be 200 and contain Status Up")
  public void thenResponseShouldBe200AndContainStatusUp() {
    then()
        .assertThat()
        .statusCode(HttpStatus.OK.value())
        .body("status", equalToIgnoringCase("up"));
  }

}
