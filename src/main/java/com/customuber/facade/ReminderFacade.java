package com.customuber.facade;

import com.customuber.bean.GeoLocation;
import com.customuber.exception.ReminderFacadeException;

import java.time.LocalDateTime;

public interface ReminderFacade {

    /**
     * Schedules reminder
     * @param source geo location of source
     * @param destination geo location of destination
     * @param arrivalDateTime report date time of user
     * @param user email id of user
     * @throws ReminderFacadeException when error occur while scheduling
     */
    void scheduleReminder(GeoLocation source, GeoLocation destination, LocalDateTime arrivalDateTime, String user) throws ReminderFacadeException;
}
