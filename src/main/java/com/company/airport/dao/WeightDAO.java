package com.company.airport.dao;

import com.company.airport.models.flightapi.FlightDetails;
import com.company.airport.models.weightapi.FlightWeight;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WeightDAO {

    private static final Logger LOGGER = LogManager.getLogger(WeightDAO.class);
    private final transient FileReaderDao fileReaderDao;
    private final transient ObjectMapper objectMapper;
    private Map<Integer, FlightWeight> flightWeightMap;

    @Autowired
    public WeightDAO(
            final FileReaderDao fileReaderDao
    ) {
        this.fileReaderDao = fileReaderDao;
        this.objectMapper = new ObjectMapper();
    }

    public Map<Integer, FlightWeight> getFlightWeightMap() {
        final JsonNode flightWeightJsonNode = fileReaderDao.getFlightWeightDetails();
        flightWeightMap = new HashMap<>();

        flightWeightJsonNode.iterator().forEachRemaining(
                jsonNode -> {
                    try {
                        FlightWeight flightWeight = objectMapper.treeToValue(jsonNode, FlightWeight.class);
                        flightWeightMap.put(flightWeight.getFlightId(), flightWeight);
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error in parsing the flight details for json={}", jsonNode);
                    }
                }
        );

        return flightWeightMap;
    }

    public FlightWeight getFlightWeightDetails(final int flightId) {
        if (CollectionUtils.isEmpty(flightWeightMap)) {
            flightWeightMap = getFlightWeightMap();
        }

        return flightWeightMap.getOrDefault(flightId, null);
    }
}
