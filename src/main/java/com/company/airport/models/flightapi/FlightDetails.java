package com.company.airport.models.flightapi;

import lombok.Data;

@Data
public class FlightDetails {
    private int flightId;
    private String flightNumber;
    private String departureAirportIATACode;
    private String arrivalAirportIATACode;
    private String departureDate;
}
