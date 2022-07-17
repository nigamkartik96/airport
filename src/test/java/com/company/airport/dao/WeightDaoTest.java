package com.company.airport.dao;

import com.company.airport.models.weightapi.FlightWeight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

public class WeightDaoTest {
    private WeightDAO weightDAO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        weightDAO = new WeightDAO(new FileReaderDao());
    }

    @Test
    public void testGetFlightWeightDetails_noError() {
        FlightWeight flightWeightDetails = weightDAO.getFlightWeightDetails(1);

        assert flightWeightDetails.getFlightId() == 1;
    }

    @Test
    public void testGetFlightWeightDetails_notFound() {
        FlightWeight flightWeightDetails = weightDAO.getFlightWeightDetails(11);

        assert flightWeightDetails == null;
    }
}
