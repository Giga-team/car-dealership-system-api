package com.gigateam.cardealershipsystemapi.controller;

import com.gigateam.cardealershipsystemapi.common.dto.ApiResponse;
import com.gigateam.cardealershipsystemapi.common.dto.Responses;
import com.gigateam.cardealershipsystemapi.common.dto.car.CarDto;
import com.gigateam.cardealershipsystemapi.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CarController extends AbstractController {

  private final CarService carService;

  @GetMapping("/cars/{id}")
  @Operation(
      tags = {"CAR"},
      summary = "Endpoint to retrieve car by id",
      responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(useReturnTypeSchema = true)}
  )
  public ResponseEntity<ApiResponse<CarDto>> getCarById(@PathVariable("id") Long id) {
    log.info("Request on retrieving car by id. Id: {}", id);

    return carService.getCarById(id)
        .map(dto -> new ResponseEntity<>(Responses.ok(dto), HttpStatus.OK))
        .orElseGet(() ->
            new ResponseEntity<>(
                Responses.notFound(String.format(ResponseMessages.CAR_BY_ID_NOT_FOUND_FORMAT, id)),
                HttpStatus.NOT_FOUND
            )
        );
  }

  @PostMapping("/cars")
  @Operation(
      tags = {"CAR"},
      summary = "Endpoint to create car",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(schema = @Schema(implementation = CarDto.class))}),
      responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(useReturnTypeSchema = true)}
  )
  public ResponseEntity<ApiResponse<Long>> createCar(@RequestBody CarDto dto) {
    log.info("Request on creating car. Car: {}", dto);

    Long newId = carService.createCar(dto);

    return new ResponseEntity<>(Responses.created(newId), HttpStatus.CREATED);
  }

  @PutMapping("/cars/{id}")
  @Operation(
      tags = {"CAR"},
      summary = "Endpoint to update car",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(schema = @Schema(implementation = CarDto.class))}),
      responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(useReturnTypeSchema = true)}
  )
  public ApiResponse<Long> updateCar(@PathVariable("id") Long id, @RequestBody CarDto dto) {
    log.info("Request on updating car. Car id: {}, new data: {}", id, dto);

    Long updatedCarId = carService.updateCar(id, dto);

    return Responses.ok(updatedCarId);
  }

  @DeleteMapping("/cars/{id}")
  @Operation(
      tags = {"CAR"},
      summary = "Endpoint to delete car by id",
      responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(useReturnTypeSchema = true)}
  )
  public ResponseEntity<ApiResponse<Void>> deleteCarById(@PathVariable("id") Long id) {
    log.info("Request on deleting car by id. Id: {}", id);

    return carService.deleteCarById(id)
        ? new ResponseEntity<>(Responses.noContent(), HttpStatus.NO_CONTENT)
        : new ResponseEntity<>(Responses.notFound(), HttpStatus.NOT_FOUND);
  }

  @GetMapping("/cars/page")
  @Operation(
      tags = {"CAR"},
      summary = "Endpoint to retrieve cars by rsql query",
      responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(useReturnTypeSchema = true)}
  )
  public ApiResponse<List<CarDto>> getCarsPage(
      @RequestParam(value = "query", defaultValue = "") String query,
      @RequestParam("page") Optional<Integer> pageParameter,
      @RequestParam("limit") Optional<Integer> limitParameter
  ) {
    int page = pageParameter.orElse(DEFAULT_PAGE_PARAMETER);
    int limit = limitParameter.orElse(DEFAULT_LIMIT_PARAMETER);

    log.info("Request on retrieving cars page. Query: {}, page: {}, limit: {}", query, page, limit);

    return Responses.ok(carService.getCarsPage(query, page, limit));
  }

  @GetMapping("/cars/count")
  @Operation(
      tags = {"CAR"},
      summary = "Endpoint to retrieve cars count by rsql query",
      responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(useReturnTypeSchema = true)}
  )
  public ApiResponse<Long> getCarsCount(@RequestParam(value = "query", defaultValue = "") String query) {
    log.info("Request on retrieving cars count. Query: {}", query);

    return Responses.ok(carService.getCarsCount(query));
  }

}
