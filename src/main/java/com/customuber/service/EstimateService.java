package com.customuber.service;

import com.customuber.bean.GeoLocation;
import com.customuber.exception.EstimateServiceException;

import java.time.LocalDateTime;

public interface EstimateService {

    /**
     * Returns estimated start trip time
     * @param source geo location of source
     * @param destination geo location of destination
     * @param arrivalDateTime arrival time of user
     * @param user email id of user
     * @return estimated local date time
     * @throws EstimateServiceException exception occurred while servicing
     */
    LocalDateTime estimateTripStartTime(GeoLocation source, GeoLocation destination, LocalDateTime arrivalDateTime, String user) throws EstimateServiceException;

    int estimateCabArrivalTime(GeoLocation source, String user);

}
