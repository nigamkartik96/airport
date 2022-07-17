package com.company.airport.services;

import com.company.airport.dao.FlightDao;
import com.company.airport.dao.WeightDAO;
import com.company.airport.models.flightapi.AirportResponse;
import com.company.airport.models.flightapi.FlightDetails;
import com.company.airport.models.weightapi.FlightWeight;
import com.company.airport.models.weightapi.WeightDetail;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AirportService {
    private static final Logger LOGGER = LogManager.getLogger(AirportService.class);

    private final transient FlightDao flightDao;
    private final transient WeightDAO weightDAO;

    @Autowired
    public AirportService(
            final FlightDao flightDao,
            final WeightDAO weightDAO
    ) {
        this.flightDao = flightDao;
        this.weightDAO = weightDAO;
    }

    public AirportResponse getAirportResponseDetails(final String iATAAirportCode, final String date) {
        final Map<String, FlightDetails> flightDetailsMappedToFlightNumber = flightDao.getFlightDetailsMappedToFlightNumber();
        if (!doesAirportExist(flightDetailsMappedToFlightNumber, iATAAirportCode)) {
            LOGGER.error("IATAAirportCode={} doesn't have any flight details", iATAAirportCode);
            return AirportResponse
                    .builder()
                    .totalNumberOfPiecesDeparting(-1)
                    .totalNumberOfPiecesArriving(-1)
                    .numberOfFlightDeparting(-1)
                    .numberOfFlightArriving(-1)
                    .error("IATAAirportCode=" + iATAAirportCode + " doesn't have any flight details")
                    .build();
        }

        return getAirportBaggageAndFlightDetails(flightDetailsMappedToFlightNumber, iATAAirportCode);
    }

    private AirportResponse getAirportBaggageAndFlightDetails(
            final Map<String, FlightDetails> flightAirportMap,
            final String iATAAirportCode
    ) {
        int flightsArrivingAtAirport = 0;
        int flightsDepartingFromAirport = 0;

        final List<Integer> arrivingFlightIds = new ArrayList<>();
        final List<Integer> departingFlightIds = new ArrayList<>();

        for (Map.Entry<String, FlightDetails> entry : flightAirportMap.entrySet()) {
            final FlightDetails flightDetails = entry.getValue();
            if (iATAAirportCode.equalsIgnoreCase(flightDetails.getArrivalAirportIATACode())) {
                flightsArrivingAtAirport = flightsDepartingFromAirport + 1;
                arrivingFlightIds.add(flightDetails.getFlightId());
            }
            if (iATAAirportCode.equalsIgnoreCase(flightDetails.getDepartureAirportIATACode())) {
                flightsDepartingFromAirport = flightsDepartingFromAirport + 1;
                departingFlightIds.add(flightDetails.getFlightId());
            }
        }

        int piecesArrivingAtAirport = totalPiecesOnAirport(arrivingFlightIds);
        int piecesDepartingFromAirport = totalPiecesOnAirport(departingFlightIds);

        return AirportResponse
                .builder()
                .numberOfFlightArriving(flightsArrivingAtAirport)
                .numberOfFlightDeparting(flightsDepartingFromAirport)
                .totalNumberOfPiecesArriving(piecesArrivingAtAirport)
                .totalNumberOfPiecesDeparting(piecesDepartingFromAirport)
                .build();
    }


    private int totalPiecesOnAirport(final List<Integer> flightIds) {
        int totalPieces = 0;
        for (final int flightId : flightIds) {
            final FlightWeight flightWeightDetails = weightDAO.getFlightWeightDetails(flightId);
            totalPieces = totalPieces + totalPiecesOnFlight(flightWeightDetails);
        }
        return totalPieces;
    }

    private int totalPiecesOnFlight(FlightWeight flightWeight) {

        int totalPieces = 0;

        if (ObjectUtils.isEmpty(flightWeight)) {
            return totalPieces;
        }

        for (final WeightDetail weightDetail : flightWeight.getBaggage()) {
            totalPieces = totalPieces + weightDetail.getPieces();
        }

        for (final WeightDetail weightDetail : flightWeight.getCargo()) {
            totalPieces = totalPieces + weightDetail.getPieces();
        }

        return totalPieces;
    }

    private boolean doesAirportExist(final Map<String, FlightDetails> flightAirportMap, final String iATAAirportCode) {
        for (Map.Entry<String, FlightDetails> entry : flightAirportMap.entrySet()) {
            final FlightDetails flightDetails = entry.getValue();
            if (
                    iATAAirportCode.equalsIgnoreCase(flightDetails.getArrivalAirportIATACode())
                            || iATAAirportCode.equalsIgnoreCase(flightDetails.getDepartureAirportIATACode())
            ) {
                return true;
            }
        }

        return false;
    }
}
