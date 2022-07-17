package com.company.airport.services;

import com.company.airport.dao.FlightDao;
import com.company.airport.dao.WeightDAO;
import com.company.airport.models.weightapi.FlightWeight;
import com.company.airport.models.weightapi.WeightResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ResourceUtils;

import java.io.File;

public class WeightServiceTest {
    @Mock
    private transient FlightDao flightDao;
    @Mock
    private transient WeightDAO weightDAO;

    private WeightService weightService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        weightService = new WeightService(flightDao, weightDAO);
    }

    private FlightWeight getFlightDetails() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = ResourceUtils.getFile("classpath:weight_response.json");
            return objectMapper.treeToValue(objectMapper.readTree(file), FlightWeight.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    public void testGetWeightResponse_noError() {
        Mockito.doReturn(1).when(flightDao).getFlightIdFromFlightNumber(ArgumentMatchers.anyString());
        Mockito.doReturn(getFlightDetails()).when(weightDAO).getFlightWeightDetails(ArgumentMatchers.anyInt());

        WeightResponse weightResponse = weightService.getWeightResponse("123", "123");
        assert weightResponse != null;
    }

    @Test
    public void testGetWeightResponse_flightNotFound() {
        Mockito.doReturn(-1).when(flightDao).getFlightIdFromFlightNumber(ArgumentMatchers.anyString());

        WeightResponse weightResponse = weightService.getWeightResponse("123", "123");
        System.out.println(weightResponse);
        assert "Flight with FlightNumber=123 not present, please check again.".equals(weightResponse.getError());
    }
}
