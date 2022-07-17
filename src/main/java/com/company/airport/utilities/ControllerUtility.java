package com.company.airport.utilities;

import com.company.airport.constants.UtilityConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ControllerUtility {
    public String weightAPIValidations(final String flightNumber, final String date) {
        if (StringUtils.isEmpty(flightNumber) || StringUtils.isEmpty(date)) {
            return "Flight Number (flightNumber) and Date (date) cannot be empty";
        }

        return checkValidityOfDate(date);
    }

    public String airportDetailsAPIAPIValidations(final String iATAAirportCode, final String date) {
        if (StringUtils.isEmpty(iATAAirportCode) || StringUtils.isEmpty(date)) {
            return "IATA Airport Code (iATAAirportCode) and Date (date) cannot be empty";
        }

        return checkValidityOfDate(date);
    }

    private String checkValidityOfDate(final String date) {
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat(UtilityConstants.DATE_FORMAT);
            formatter.parse(date);
            return null;
        } catch (ParseException e) {
            return "Enter Date (date) in UTC format (YYYY-MM-ddThh:mm:ssZ)";
        }
    }
}
