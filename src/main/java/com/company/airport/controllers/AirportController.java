package com.company.airport.controllers;

import com.company.airport.constants.ControllerConstants;
import com.company.airport.models.weightapi.WeightResponse;
import com.company.airport.services.WeightService;
import com.company.airport.utilities.ControllerUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerConstants.AIRPORT_URI)
public class AirportController {

    private static final Logger LOGGER = LogManager.getLogger(AirportController.class);

    private final transient ControllerUtility controllerUtility;
    private final transient WeightService weightService;

    @Autowired
    public AirportController(
            final ControllerUtility controllerUtility,
            final WeightService weightService
    ) {
        this.controllerUtility = controllerUtility;
        this.weightService = weightService;
    }

    @GetMapping(
            path = ControllerConstants.WEIGHT_API_URI,
            produces = {ControllerConstants.APPLICATION_JSON}
    )
    public ResponseEntity<WeightResponse> getWeight(@RequestParam String flightNumber, @RequestParam String date) {

        LOGGER.info("Request received with params: FlightNumber={} and Date={}", flightNumber, date);
        final String badRequestResponse = controllerUtility.weightAPIRequestValidation(flightNumber, date);

        if (!StringUtils.isEmpty(badRequestResponse)) {
            LOGGER.error("Bad request response, error={}", badRequestResponse);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(WeightResponse.builder().error(badRequestResponse).build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(weightService.getWeightResponse(flightNumber, date));
    }
}
