package com.company.airport.models.weightapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WeightResponse {
    private Double cargoWeight;
    private Double baggageWeight;
    private Double totalWeight;
    private String error;
}
