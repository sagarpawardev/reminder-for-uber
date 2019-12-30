package com.customuber.service;

import com.customuber.bean.Email;
import com.customuber.bean.GeoLocation;

import java.time.LocalDateTime;

public interface ContentService {

    /**
     *
     * @param emailId email id of user
     * @param source geo location of source
     * @param destination geo location of destination
     * @param reportTime reporting time of user
     * @return Prepared email
     */
    Email prepareBookingReminderEmail(String emailId, GeoLocation source, GeoLocation destination, LocalDateTime reportTime);
}
