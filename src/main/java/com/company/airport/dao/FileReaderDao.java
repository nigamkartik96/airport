package com.company.airport.dao;

import com.company.airport.constants.DAOConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;

@Component
public class FileReaderDao {

    private static final Logger LOGGER = LogManager.getLogger(FileReaderDao.class);
    private static JsonNode AIRPORT_JSON_DATA;

    static {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final File file = ResourceUtils.getFile(DAOConstants.FILE_NAME);
            AIRPORT_JSON_DATA = objectMapper.readTree(file);
        } catch (Exception e) {
            LOGGER.error("Error reading file, terminating the application");
            AIRPORT_JSON_DATA = null;
        }
    }

    public JsonNode getFlightDetails() {
        return AIRPORT_JSON_DATA.get(DAOConstants.FLIGHT_DETAILS);
    }

    public JsonNode getFlightWeightDetails() {
        return AIRPORT_JSON_DATA.get(DAOConstants.FLIGHT_WEIGHT_DETAILS);
    }
}
