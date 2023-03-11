package com.project.hangar.integration;

import com.project.hangar.common.BaseIT;
import com.project.hangar.dto.SpaceshipRequest;
import com.project.hangar.entity.SpaceshipEntity;
import com.project.hangar.integration.steps.SpaceshipSteps;
import com.project.hangar.repository.SpaceshipRepository;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.project.hangar.common.Constants.buildRequest;

@ExtendWith({SerenityJUnit5Extension.class})
class SpaceshipIT extends BaseIT {

  private List<SpaceshipEntity> spaceshipsBefore;

  @Autowired
  private SpaceshipRepository spaceshipRepository;

  @Steps
  private SpaceshipSteps spaceshipSteps;

  @LocalServerPort
  private int port;

  @PostConstruct
  public void setDefaultPort() {
    SerenityRest.setDefaultPort(port);
  }

  @BeforeEach
  void setUp() {
    spaceshipsBefore = spaceshipRepository.findAll();
  }

  @DisplayName("Get Spaceships")
  @Test
  void getSpaceships_happyPath() {
    spaceshipSteps.givenSpaceshipsExist(spaceshipsBefore);
    spaceshipSteps.whenGetRequestIsSent();
    spaceshipSteps.thenResponseShouldBe200AndContainAllSpaceships();
  }

  @DisplayName("Get Spaceship by Id")
  @Test
  void getSpaceshipById_happyPath() {
    final SpaceshipEntity expected = spaceshipsBefore.get(0);

    spaceshipSteps.givenSpaceshipsExist(spaceshipsBefore);
    spaceshipSteps.whenGetRequestIsSentWithId(expected.getId());
    spaceshipSteps.thenResponseShouldBe200AndContainCorrectSpaceship(expected);
  }

  @DisplayName("Add Spaceship")
  @Test
  void addSpaceship_happyPath() {
    final SpaceshipRequest requestBody = buildRequest();

    spaceshipSteps.givenSpaceshipRequest(requestBody);
    spaceshipSteps.whenPostRequestIsSent();
    spaceshipSteps.thenNewSpaceshipShouldBeAddedInDatabase(spaceshipsBefore, findAllSpaceshipsAfter());
    spaceshipSteps.thenResponseShouldBe201AndContainNewlyAddedSpaceship();
  }

  @DisplayName("Update Spaceship by Id")
  @Test
  void updateSpaceshipById_happyPath() {
    final SpaceshipRequest request = buildRequest();
    final SpaceshipEntity spaceshipToBeUpdated = spaceshipsBefore.get(0);

    spaceshipSteps.givenSpaceshipsExist(spaceshipsBefore);
    spaceshipSteps.givenSpaceshipRequest(request);
    spaceshipSteps.whenPutRequestIsSentWithId(spaceshipToBeUpdated.getId());
    spaceshipSteps.thenCorrectSpaceshipShouldBeUpdated(findAllSpaceshipsAfter(), spaceshipToBeUpdated, request);
    spaceshipSteps.thenResponseShouldBe200AndContainUpdatedSpaceship(spaceshipToBeUpdated.getId());
  }

  @DisplayName("Delete Spaceship by Id")
  @Test
  void deleteSpaceshipById_happyPath() {
    final SpaceshipEntity spaceshipToBeDeleted = spaceshipsBefore.get(0);

    spaceshipSteps.givenSpaceshipsExist(spaceshipsBefore);
    spaceshipSteps.whenDeleteRequestIsSentWithId(spaceshipToBeDeleted.getId());
    spaceshipSteps.thenCorrectSpaceshipShouldBeDeleted(findAllSpaceshipsAfter(), spaceshipToBeDeleted);
    spaceshipSteps.thenResponseShouldBe204();
  }

  private List<SpaceshipEntity> findAllSpaceshipsAfter() {
    return spaceshipRepository.findAll();
  }
}
