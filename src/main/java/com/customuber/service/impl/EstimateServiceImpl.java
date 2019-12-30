package com.customuber.service.impl;

import com.customuber.bean.GeoLocation;
import com.customuber.client.uberapi.UberApiClient;
import com.customuber.client.uberapi.model.TimeEstimate;
import com.customuber.client.uberapi.model.TimeEstimateList;
import com.customuber.service.ApiLoggingService;
import com.customuber.service.ConfigurationService;
import com.customuber.service.EstimateService;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EstimateServiceImpl implements EstimateService {

    private static final String GOOGLE_API_KEY = "google_api_key";
    private static final String KEY_UBER_API_URL = "uber_api_url";

    private ConfigurationService configurationService;
    private ApiLoggingService apiLoggingService;


    @Override
    public LocalDateTime estimateTripStartTime(GeoLocation source, GeoLocation destination, LocalDateTime arrivalDateTime, String user) {
        long drivingTime = findDrivingTimeInSecs(source, destination, arrivalDateTime, user);
        return arrivalDateTime.minusSeconds(drivingTime);
    }

    @Override
    public int estimateCabArrivalTime(GeoLocation source, String user) {
        String uberApiUrl = configurationService.getProperty(KEY_UBER_API_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(uberApiUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        int cabArrivalTime = -1;
        UberApiClient apiClient = retrofit.create(UberApiClient.class);
        try {
            apiLoggingService.log("uber", user);
            TimeEstimateList timeEstimateList = apiClient.getEstimates(source.getLatitude(), source.getLongitude()).execute().body();
            List<TimeEstimate> timeEstimates;
            if (timeEstimateList != null) {
                timeEstimates = timeEstimateList.getTimes();
            } else {
                timeEstimates = Collections.emptyList();
            }

            Optional<TimeEstimate> tCabArrivalTime = timeEstimates.stream().filter(timeEstimate -> timeEstimate.getDisplayName().equals("uberGO")).findFirst();
            if (tCabArrivalTime.isPresent()) {
                cabArrivalTime = tCabArrivalTime.get().getEstimate();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cabArrivalTime;
    }

    private long findDrivingTimeInSecs(GeoLocation source, GeoLocation destination, LocalDateTime arrivalDateTime, String user) {

        LatLng latLngSource = new LatLng();
        latLngSource.lat = source.getLatitude();
        latLngSource.lng = source.getLongitude();

        LatLng latLngDestination = new LatLng();
        latLngDestination.lat = destination.getLatitude();
        latLngDestination.lng = destination.getLongitude();

        String googleApiKey = configurationService.getProperty(GOOGLE_API_KEY);
        final GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiKey).build();
        DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context);
        request.arrivalTime(arrivalDateTime.toInstant(ZoneOffset.UTC))
                .origins(latLngSource)
                .destinations(latLngDestination)
                .mode(TravelMode.DRIVING);

        long maxDurationInSecs = 0;
        try {
            apiLoggingService.log("google", user);
            DistanceMatrix matrix = request.await();

            if (matrix.rows.length > 0) {
                DistanceMatrixRow firstRow = matrix.rows[0];
                for (int i = 0; i < firstRow.elements.length; i++) {
                    DistanceMatrixElement element = firstRow.elements[i];
                    long reqSeconds = element.duration.inSeconds;
                    if (maxDurationInSecs < reqSeconds) {
                        maxDurationInSecs = reqSeconds;
                    }
                }
            }

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();

        }

        return maxDurationInSecs;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setApiLoggingService(ApiLoggingService apiLoggingService) {
        this.apiLoggingService = apiLoggingService;
    }
}
