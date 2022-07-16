package com.company.airport.models.weightapi;

import lombok.Data;

import java.util.List;

@Data
public class FlightWeight {
    private int flightId;
    private List<WeightDetail> baggage;
    private List<WeightDetail> cargo;
}
