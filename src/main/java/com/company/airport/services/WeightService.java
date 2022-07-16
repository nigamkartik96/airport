package com.company.airport.services;

import com.company.airport.dao.FlightDao;
import com.company.airport.dao.WeightDAO;
import com.company.airport.models.weightapi.FlightWeight;
import com.company.airport.models.weightapi.WeightDetail;
import com.company.airport.models.weightapi.WeightResponse;
import com.company.airport.models.weightapi.WeightUnit;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class WeightService {

    private static final Logger LOGGER = LogManager.getLogger(WeightService.class);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final transient FlightDao flightDao;
    private final transient WeightDAO weightDAO;

    @Autowired
    public WeightService(
            final FlightDao flightDao,
            final WeightDAO weightDAO
    ) {
        this.flightDao = flightDao;
        this.weightDAO = weightDAO;
    }

    public WeightResponse getWeightResponse(String flightNumber, String Date) {
        int flightId = flightDao.getFlightIdFromFlightNumber(flightNumber);
        LOGGER.info("FlightId={} found for FlightNumber={}", flightId, flightNumber);
        if (flightId == -1) {
            LOGGER.error("FlightNumber={}, not found", flightNumber);
            return WeightResponse
                    .builder()
                    .error("Flight with FlightNumber=" + flightNumber + " not present, please check again.")
                    .build();
        }

        final FlightWeight flightWeightDetails = weightDAO.getFlightWeightDetails(flightId);
        LOGGER.info("For FlightId={}, WeightDetails={}", flightId, flightWeightDetails);

        if (ObjectUtils.isEmpty(flightWeightDetails)) {
            LOGGER.error("For FlightId={} and FlightNumber={}, Weight Details not found", flightId, flightNumber);
            return WeightResponse
                    .builder()
                    .error("Flight with FlightNumber=" + flightNumber + " weight details not found.")
                    .build();
        }

        Double baggageWeightInKg = calculateTotalWeight(flightWeightDetails.getBaggage());
        Double cargoWeightInKg = calculateTotalWeight(flightWeightDetails.getCargo());

        return WeightResponse
                .builder()
                .baggageWeight(Double.parseDouble(DECIMAL_FORMAT.format(baggageWeightInKg)))
                .cargoWeight(Double.parseDouble(DECIMAL_FORMAT.format(cargoWeightInKg)))
                .totalWeight(Double.parseDouble(DECIMAL_FORMAT.format(baggageWeightInKg + cargoWeightInKg)))
                .build();
    }

    private double calculateTotalWeight(final List<WeightDetail> weightDetails) {
        double weightInKg = 0.0;

        if (CollectionUtils.isEmpty(weightDetails)) {
            return weightInKg;
        }

        for (final WeightDetail weightDetail : weightDetails) {
            final int weight = weightDetail.getWeight();
            final WeightUnit weightUnit = weightDetail.getWeightUnit();

            if (weightUnit == WeightUnit.lb) {
                weightInKg += getWeightInKg(weight);
            } else {
                weightInKg += weight;
            }
        }

        return weightInKg;
    }

    private double getWeightInKg(final int weightInLb) {
        return 0.45359237 * weightInLb;
    }
}
