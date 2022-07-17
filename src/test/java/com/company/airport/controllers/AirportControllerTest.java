package com.company.airport.controllers;

import com.company.airport.models.flightapi.AirportResponse;
import com.company.airport.models.weightapi.WeightResponse;
import com.company.airport.services.AirportService;
import com.company.airport.services.WeightService;
import com.company.airport.utilities.ControllerUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AirportControllerTest {

    @Mock
    private transient WeightService weightService;
    @Mock
    private transient AirportService airportService;
    private AirportController airportController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        airportController = new AirportController(new ControllerUtility(), weightService, airportService);
    }

    @Test
    public void testWeightAPI_emptyFlightDetails() {
        ResponseEntity<WeightResponse> responseEntity = airportController.getWeight("", "");
        assert responseEntity.getBody() != null;
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert "Flight Number (flightNumber) and Date (date) cannot be empty".equals(responseEntity.getBody().getError());
    }

    @Test
    public void testWeightAPI_incorrectDateFormat() {
        ResponseEntity<WeightResponse> responseEntity = airportController.getWeight("123", "1323");
        assert responseEntity.getBody() != null;
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert "Enter Date (date) in UTC format (YYYY-MM-ddThh:mm:ssZ)".equals(responseEntity.getBody().getError());
    }

    @Test
    public void testWeightAPI_noError() {
        Mockito.doReturn(
                WeightResponse.builder().error(null).build()
        ).when(weightService).getWeightResponse(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        ResponseEntity<WeightResponse> responseEntity = airportController.getWeight("123", "2020-09-01T02:02:02Z");
        assert responseEntity.getBody() != null;
        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testAirportDetailAPI_emptyDetails() {
        ResponseEntity<AirportResponse> responseEntity = airportController.getAirportDetailsAPI("", "");
        assert responseEntity.getBody() != null;
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert "IATA Airport Code (iATAAirportCode) and Date (date) cannot be empty".equals(responseEntity.getBody().getError());
    }

    @Test
    public void testAirportDetailAPI_incorrectDateFormat() {
        ResponseEntity<AirportResponse> responseEntity = airportController.getAirportDetailsAPI("123", "1323");
        assert responseEntity.getBody() != null;
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert "Enter Date (date) in UTC format (YYYY-MM-ddThh:mm:ssZ)".equals(responseEntity.getBody().getError());
    }

    @Test
    public void testAirportDetailAPI_noError() {
        Mockito.doReturn(AirportResponse.builder().build()).when(airportService)
                .getAirportResponseDetails(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        ResponseEntity<AirportResponse> responseEntity = airportController
                .getAirportDetailsAPI("123", "2020-09-01T02:02:02Z");
        assert responseEntity.getBody() != null;
        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
