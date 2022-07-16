package com.company.airport.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlightDao {

    private static final Logger LOGGER = LogManager.getLogger(FlightDao.class);
    private final transient FileReaderDao fileReaderDao;

    @Autowired
    public FlightDao(
            final FileReaderDao fileReaderDao
    ) {
        this.fileReaderDao = fileReaderDao;
    }


}
