package com.project.cantina.service;

import com.project.cantina.dto.PilotDto;
import com.project.cantina.entity.PilotEntity;
import com.project.cantina.exception.NotFoundException;
import com.project.cantina.mapper.PilotMapper;
import com.project.cantina.repository.PilotRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class PilotServiceImpl implements PilotService {

  public static final String NOT_FOUND_MESSAGE = "Pilot not found";

  public static final String NOT_FOUND_ID_PROVIDED_MESSAGE = "Pilot with id {} not found";

  private final PilotRepository pilotRepository;

  private final PilotMapper pilotMapper;

  public PilotServiceImpl(final PilotRepository pilotRepository, final PilotMapper pilotMapper) {
    this.pilotRepository = pilotRepository;
    this.pilotMapper = pilotMapper;
  }

  @Override
  public List<PilotDto> getAll() {

    final List<PilotDto> pilotDtos = pilotRepository.findAll().stream()
        .map(pilotMapper::entityToDto)
        .toList();
    log.info(pilotDtos.isEmpty() ? "No pilots found" : "Pilots found");

    return pilotDtos;
  }

  @Override
  public List<PilotDto> getAllByIds(final List<UUID> pilotIds) {

    final List<PilotDto> pilotDtos = pilotRepository.findAllByIdIn(pilotIds).stream()
        .map(pilotMapper::entityToDto)
        .toList();
    log.info(pilotDtos.isEmpty() ? "No pilots found" : "Pilots found");

    return pilotDtos;
  }

  @Override
  public PilotDto getById(final UUID pilotId) {

    final PilotDto pilotDto = pilotRepository.findById(pilotId)
        .map(pilotMapper::entityToDto)
        .orElseThrow(() -> {
          log.info(NOT_FOUND_ID_PROVIDED_MESSAGE, pilotId);
          throw new NotFoundException(NOT_FOUND_MESSAGE);
        });
    log.info("Pilot with id {} found", pilotId);

    return pilotDto;
  }

  @Override
  public PilotDto add(final PilotDto pilotDto) {

    final PilotEntity savedPilot = pilotRepository.save(pilotMapper.dtoToEntity(pilotDto));
    log.info("Pilot added: {}", savedPilot);

    return pilotMapper.entityToDto(savedPilot);
  }

  @Override
  @Transactional
  public PilotDto updateById(final PilotDto pilotDtoUpdate, final UUID pilotId) {
    final PilotEntity pilotToBeUpdated = pilotRepository.findById(pilotId)
        .orElseThrow(() -> {
          log.info(NOT_FOUND_ID_PROVIDED_MESSAGE, pilotId);
          throw new NotFoundException(NOT_FOUND_MESSAGE);
        });

    pilotMapper.updateEntityWithDto(pilotToBeUpdated, pilotDtoUpdate);
    final PilotEntity updatedPilotEntity = pilotRepository.save(pilotToBeUpdated);
    final PilotDto updatedPilotDto = pilotMapper.entityToDto(updatedPilotEntity);
    log.info("Pilot updated: {}", updatedPilotDto);

    return updatedPilotDto;
  }

  @Override
  public void deleteById(final UUID pilotId) {

    final Optional<PilotEntity> pilotToBeDeleted = pilotRepository.findById(pilotId);

    if (pilotToBeDeleted.isEmpty()) {
      log.info(NOT_FOUND_ID_PROVIDED_MESSAGE, pilotId);
      throw new NotFoundException(NOT_FOUND_MESSAGE);
    }

    pilotRepository.deleteById(pilotId);
    log.info("Pilot with id {} deleted", pilotId);
  }
}
