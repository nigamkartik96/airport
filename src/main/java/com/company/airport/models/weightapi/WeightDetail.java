package com.company.airport.models.weightapi;

import lombok.Data;

@Data
public class WeightDetail {
    private int id;
    private int weight;
    private WeightUnit weightUnit;
    private int pieces;
}
