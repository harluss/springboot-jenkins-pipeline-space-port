package com.project.hangar.integration.steps;

import com.project.hangar.common.TestUtil;
import com.project.hangar.dto.SpaceshipRequest;
import com.project.hangar.dto.SpaceshipResponse;
import com.project.hangar.entity.SpaceshipEntity;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.then;
import static net.serenitybdd.rest.SerenityRest.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class SpaceshipSteps extends TestUtil {

  private static final String TEST_API = "/api/v1/spaceships";

  private static final String TEST_API_BY_ID = "/api/v1/spaceships/{id}";

  private List<SpaceshipEntity> spaceshipsBefore;

  private List<SpaceshipEntity> spaceshipsAfter;

  private SpaceshipRequest spaceshipRequestBody;

  @Given(" Spaceships exist")
  public void givenSpaceshipsExist(final List<SpaceshipEntity> allSpaceshipsBefore) {
    given();

    spaceshipsBefore = allSpaceshipsBefore;
  }

  @When(" GET request is sent")
  public void whenGetRequestIsSent() {
    when()
        .get(TEST_API);
  }

  @Then("response should be 200 and contain all Spaceships")
  public void thenResponseShouldBe200AndContainAllSpaceships() {
    final List<SpaceshipResponse> actual = then()
        .assertThat()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body()
        .as(new TypeRef<>() {
        });

    assertThat(actual).hasSize(spaceshipsBefore.size());
  }

  @When(" GET request is sent with SpaceshipId {0}")
  public void whenGetRequestIsSentWithId(final UUID spaceshipId) {
    when()
        .get(TEST_API_BY_ID, spaceshipId);
  }

  @Then("response should be 200 and contain correct Spaceship")
  public void thenResponseShouldBe200AndContainCorrectSpaceship(final SpaceshipEntity expected) {
    final SpaceshipEntity actual = then()
        .assertThat()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body()
        .as(SpaceshipEntity.class);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Given(" Spaceship request")
  public void givenSpaceshipRequest(final SpaceshipRequest requestBody) {
    given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(objectToJsonString(requestBody));

    spaceshipRequestBody = requestBody;
  }

  @When(" POST request is sent")
  public void whenPostRequestIsSent() {
    when()
        .post(TEST_API);
  }

  @Then("new Spaceship should be added in database")
  public void thenNewSpaceshipShouldBeAddedInDatabase(final List<SpaceshipEntity> allSpaceshipsBefore,
                                                      final List<SpaceshipEntity> allSpaceshipsAfter) {
    spaceshipsBefore = allSpaceshipsBefore;
    spaceshipsAfter = allSpaceshipsAfter;

    assertThat(spaceshipsAfter).hasSize(spaceshipsBefore.size() + 1);
  }

  @Then("response should be 201 and contain newly added Spaceship")
  public void thenResponseShouldBe201AndContainNewlyAddedSpaceship() {
    final SpaceshipEntity actual = then()
        .assertThat()
        .statusCode(HttpStatus.CREATED.value())
        .header(HttpHeaders.LOCATION, notNullValue())
        .extract()
        .body()
        .as(SpaceshipEntity.class);

    assertThat(spaceshipsBefore).doesNotContain(actual);
    assertThat(spaceshipsAfter).contains(actual);
    assertThat(spaceshipRequestBody).usingRecursiveComparison().isEqualTo(actual);
  }

  @When(" PUT request is sent with SpaceshipId {0}")
  public void whenPutRequestIsSentWithId(final UUID spaceshipId) {
    when()
        .put(TEST_API_BY_ID, spaceshipId);
  }

  @Then("correct Spaceship should be updated")
  public void thenCorrectSpaceshipShouldBeUpdated(final List<SpaceshipEntity> allSpaceshipsAfter,
                                                  final SpaceshipEntity spaceshipToBeUpdated,
                                                  final SpaceshipRequest requestBody) {
    spaceshipsAfter = allSpaceshipsAfter;

    assertThat(spaceshipsBefore).contains(spaceshipToBeUpdated);
    assertThat(spaceshipsAfter).hasSameSizeAs(spaceshipsBefore)
        .doesNotContain(spaceshipToBeUpdated)
        .extracting(SpaceshipEntity::getId)
        .contains(spaceshipToBeUpdated.getId());
  }

  @Then("response should be 200 and contain updated Spaceship")
  public void thenResponseShouldBe200AndContainUpdatedSpaceship(final UUID spaceshipToBeUpdatedId) {
    final SpaceshipEntity actual = then()
        .assertThat()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body()
        .as(SpaceshipEntity.class);

    assertThat(actual.getId()).isEqualTo(spaceshipToBeUpdatedId);
    assertThat(actual).usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(spaceshipRequestBody);
  }

  @When(" DELETE request is sent with SpaceshipId {0}")
  public void whenDeleteRequestIsSentWithId(final UUID spaceshipToBeDeletedId) {
    when()
        .delete(TEST_API_BY_ID, spaceshipToBeDeletedId);
  }

  public void thenCorrectSpaceshipShouldBeDeleted(final List<SpaceshipEntity> allSpaceshipsAfter,
                                                  final SpaceshipEntity spaceshipToBeDeleted) {
    spaceshipsAfter = allSpaceshipsAfter;

    assertThat(spaceshipsAfter)
        .doesNotContain(spaceshipToBeDeleted)
        .hasSize(spaceshipsBefore.size() - 1);
  }

  @Then("response should be 204")
  public void thenResponseShouldBe204() {
    then()
        .assertThat()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }
}
