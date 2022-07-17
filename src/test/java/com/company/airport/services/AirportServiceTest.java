package com.company.airport.services;

import com.company.airport.dao.FlightDao;
import com.company.airport.dao.WeightDAO;
import com.company.airport.models.flightapi.AirportResponse;
import com.company.airport.models.flightapi.FlightDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AirportServiceTest {
    @Mock
    private transient FlightDao flightDao;
    @Mock
    private transient WeightDAO weightDAO;

    private AirportService airportService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        airportService = new AirportService(flightDao, weightDAO);
    }

    private Map<String, FlightDetails> getAirportResponse() {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final File file = ResourceUtils.getFile("classpath:flight_response.json");
            final JsonNode jsonNode = objectMapper.readTree(file);

            final Map<String, FlightDetails> flightDetailsMap = new HashMap<>();
            jsonNode.iterator().forEachRemaining(
                    node -> {
                        try {
                            FlightDetails flightDetails = objectMapper.treeToValue(node, FlightDetails.class);
                            flightDetailsMap.put(flightDetails.getFlightNumber(), flightDetails);
                        } catch (Exception e) {
                        }
                    }
            );
            return flightDetailsMap;
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    public void test() {
        Map<String, FlightDetails> airportResponse = getAirportResponse();
        Mockito.doReturn(airportResponse).when(flightDao).getFlightDetailsMappedToFlightNumber();
        AirportResponse laxResponse = airportService.getAirportResponseDetails("LAX", "");
        assert laxResponse != null;
        assert laxResponse.getError() == null;
    }
}
