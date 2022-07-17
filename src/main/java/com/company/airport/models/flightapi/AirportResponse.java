package com.company.airport.models.flightapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AirportResponse {
    private int numberOfFlightDeparting;
    private int numberOfFlightArriving;
    private int totalNumberOfPiecesArriving;
    private int totalNumberOfPiecesDeparting;
    private String error;
}
