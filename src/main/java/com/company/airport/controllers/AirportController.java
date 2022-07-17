package com.company.airport.controllers;

import com.company.airport.constants.ControllerConstants;
import com.company.airport.models.flightapi.AirportResponse;
import com.company.airport.models.weightapi.WeightResponse;
import com.company.airport.services.AirportService;
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
@RequestMapping(ControllerConstants.AIRPORT_CONTROLLER_URI)
public class AirportController {

    private static final Logger LOGGER = LogManager.getLogger(AirportController.class);

    private final transient ControllerUtility controllerUtility;
    private final transient WeightService weightService;
    private final transient AirportService airportService;


    @Autowired
    public AirportController(
            final ControllerUtility controllerUtility,
            final WeightService weightService,
            final AirportService airportService
    ) {
        this.controllerUtility = controllerUtility;
        this.weightService = weightService;
        this.airportService = airportService;
    }

    @GetMapping(
            path = ControllerConstants.WEIGHT_API_URI,
            produces = {ControllerConstants.APPLICATION_JSON}
    )
    public ResponseEntity<WeightResponse> getWeight(@RequestParam String flightNumber, @RequestParam String date) {

        LOGGER.info("method=getWeight; Request received with params: FlightNumber={} and Date={}", flightNumber, date);
        final String badRequestResponse = controllerUtility.weightAPIValidations(flightNumber, date);

        if (!StringUtils.isEmpty(badRequestResponse)) {
            LOGGER.error("Bad request response, error={}", badRequestResponse);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(WeightResponse.builder().error(badRequestResponse).build());
        }

        final WeightResponse weightResponse = weightService.getWeightResponse(flightNumber, date);
        LOGGER.info(
                "method=getWeight; Returning Weight Details for FlightNumber={}, WeightDetails={}",
                flightNumber, weightResponse
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(weightResponse);
    }

    @GetMapping(
            path = ControllerConstants.AIRPORT_API_URI,
            produces = {ControllerConstants.APPLICATION_JSON}
    )
    public ResponseEntity<AirportResponse> getAirportDetailsAPI(
            @RequestParam String iATAAirportCode, @RequestParam String date
    ) {
        LOGGER.info("method=getAirportDetailsAPI; Request received with params: IATAAirportCode={} and Date={}",
                iATAAirportCode, date);
        final String badRequestResponse = controllerUtility.airportDetailsAPIAPIValidations(iATAAirportCode, date);

        if (!StringUtils.isEmpty(badRequestResponse)) {
            LOGGER.error("Bad request response, error={}", badRequestResponse);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(AirportResponse.builder()
                            .totalNumberOfPiecesDeparting(-1)
                            .totalNumberOfPiecesArriving(-1)
                            .numberOfFlightDeparting(-1)
                            .numberOfFlightArriving(-1)
                            .error(badRequestResponse).build());
        }

        final AirportResponse airportResponse = airportService.getAirportResponseDetails(iATAAirportCode, date);
        LOGGER.info("method=getAirportDetailsAPI; Returning the AirportResponse={}", airportResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(airportResponse);
    }
}
