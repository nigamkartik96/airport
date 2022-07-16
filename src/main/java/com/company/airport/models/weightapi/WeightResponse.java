package com.company.airport.models.weightapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
public class WeightResponse {
    public Long cargoWeight;
    public Long baggageWeight;
    public Long totalWeight;
    public String error;
}
