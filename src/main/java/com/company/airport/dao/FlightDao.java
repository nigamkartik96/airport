package com.company.airport.dao;

import com.company.airport.models.flightapi.FlightDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FlightDao {

    private static final Logger LOGGER = LogManager.getLogger(FlightDao.class);
    private final transient FileReaderDao fileReaderDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public FlightDao(
            final FileReaderDao fileReaderDao
    ) {
        this.fileReaderDao = fileReaderDao;
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, FlightDetails> getFlightDetailsMappedToFlightNumber() {
        final JsonNode flightDetailsJsonNode = fileReaderDao.getFlightDetails();
        final Map<String, FlightDetails> flightDetailsMap = new HashMap<>();
        flightDetailsJsonNode.elements().forEachRemaining(
                jsonNode -> {
                    try {
                        final FlightDetails flightDetails = objectMapper.treeToValue(jsonNode, FlightDetails.class);
                        flightDetailsMap.put(flightDetails.getFlightNumber(), flightDetails);
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error in processing: FlightJson={}", jsonNode);
                        throw new RuntimeException(e);
                    }
                }
        );

        return flightDetailsMap;
    }

    public int getFlightIdFromFlightNumber(String flightNumber) {
        final Map<String, FlightDetails> flightDetailsMap = getFlightDetailsMappedToFlightNumber();
        if (flightDetailsMap.containsKey(flightNumber)) {
            return flightDetailsMap.get(flightNumber).getFlightId();
        }

        return -1;
    }
}
