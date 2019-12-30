package com.customuber.service.impl;

import com.customuber.bean.Email;
import com.customuber.bean.GeoLocation;
import com.customuber.service.ConfigurationService;
import com.customuber.service.ContentService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ContentServiceImpl implements ContentService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh : mm a");

    private ConfigurationService configurationService;

    @Override
    public Email prepareBookingReminderEmail(String emailId, GeoLocation source, GeoLocation destination, LocalDateTime reportTime) {
        String subject = "Time to book Uber";

        //formatter.withZone(ZoneId.of("UTC"));
        ZonedDateTime zonedDateTime = reportTime.atZone(ZoneId.systemDefault());
        zonedDateTime = zonedDateTime.withZoneSameInstant(configurationService.getZone());
        String time = zonedDateTime.format(formatter);

        String body = "I am in ("+ source.getLatitude()+"," +source.getLongitude()+") and need to be in ("
                +destination.getLatitude()+","+ source.getLongitude()+" ) at "+time+" for a meeting.";

        return new Email(emailId, subject, body);
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
