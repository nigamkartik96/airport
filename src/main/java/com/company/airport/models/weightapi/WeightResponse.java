package com.company.airport.models.weightapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WeightResponse {
    public Double cargoWeight;
    public Double baggageWeight;
    public Double totalWeight;
    public String error;
}
