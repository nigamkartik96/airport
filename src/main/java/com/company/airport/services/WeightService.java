package com.company.airport.services;

import com.company.airport.dao.FlightDao;
import com.company.airport.models.weightapi.WeightResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeightService {

    private static final Logger LOGGER = LogManager.getLogger(WeightService.class);
    private final transient FlightDao flightDao;

    @Autowired
    public WeightService(
            final FlightDao flightDao
    ) {
        this.flightDao = flightDao;
    }

    public WeightResponse getWeightResponse(String flightNumber, String Date) {
        int flightId = flightDao.getFlightIdFromFlightNumber(flightNumber);
        LOGGER.info("FlightId={} found for FlightNumber={}", flightId, flightNumber);
        if (flightId == -1) {
            LOGGER.error("FlightNumber={}, not found", flightNumber);
            return WeightResponse
                    .builder()
                    .error("Flight with FlightNumber=" + flightNumber + " not present, please check again.")
                    .build();
        }


        return null;
    }
}
