package com.company.airport.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

public class FlightDaoTest {
    private FlightDao flightDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        flightDao = new FlightDao(new FileReaderDao());
    }

    @Test
    public void testGetFlightIdFromFlightNumber_flightNotPresent() {
        assert flightDao.getFlightIdFromFlightNumber("123") == -1;
    }

    @Test
    public void testGetFlightIdFromFlightNumber_flightPresent() {
        assert flightDao.getFlightIdFromFlightNumber("7755") == 0;
    }
}
