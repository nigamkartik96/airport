package com.company.airport.utilities;

import com.company.airport.constants.UtilityConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ControllerUtility {
    public String weightAPIRequestValidation(String flightNumber, String date) {
        if (StringUtils.isEmpty(flightNumber) || StringUtils.isEmpty(date)) {
            return "Flight Number (flightNumber) and Date (date) cannot be empty";
        }

        try {
            final SimpleDateFormat formatter = new SimpleDateFormat(UtilityConstants.DATE_FORMAT);
            final Date flightDate = formatter.parse(date);
        } catch (ParseException e) {
            return "Enter Date (date) in UTC format (YYYY-MM-ddThh:mm:ssZ)";
        }

        return null;
    }
}
