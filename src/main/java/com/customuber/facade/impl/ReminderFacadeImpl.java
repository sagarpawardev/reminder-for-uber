package com.customuber.facade.impl;

import com.customuber.bean.Email;
import com.customuber.bean.GeoLocation;
import com.customuber.exception.EstimateServiceException;
import com.customuber.exception.ReminderFacadeException;
import com.customuber.facade.ReminderFacade;
import com.customuber.service.ContentService;
import com.customuber.service.EstimateService;
import com.customuber.service.ReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderFacadeImpl implements ReminderFacade {

    private static final Logger LOG = LoggerFactory.getLogger(ReminderFacadeImpl.class);

    private static final double DECAY_FACTOR = 0.2d;

    private EstimateService estimateService;
    private ReminderService reminderService;
    private ContentService contentService;

    @Override
    public void scheduleReminder(GeoLocation source, GeoLocation destination, LocalDateTime arrivalDateTime, String user) throws ReminderFacadeException {
        try {
            LocalDateTime tripStartTime = estimateService.estimateTripStartTime(source, destination, arrivalDateTime, user);
            if(tripStartTime.isBefore(LocalDateTime.now()))
                throw new ReminderFacadeException("start trip time already passed");

            checkAndRemind(source, destination, tripStartTime, -1, user, arrivalDateTime);

        } catch (EstimateServiceException e) {
            throw new ReminderFacadeException(e.getMessage());
        }


    }

    //<editor-fold desc = "Private Methods">
    private void checkAndRemind(GeoLocation source, GeoLocation destination, LocalDateTime tripStartTime, long estimatedCabArrivalTime, String userEmail, LocalDateTime reportTime){
        long cabArrivalTime = estimateService.estimateCabArrivalTime(source, userEmail);
        cabArrivalTime = normalizeCabArrivalTime(estimatedCabArrivalTime, cabArrivalTime);
        LocalDateTime bookingTime = tripStartTime.minusSeconds(cabArrivalTime);

        long timeLeft = Duration.between(LocalDateTime.now(), bookingTime).toMinutes();
        if(timeLeft > 60){
            LOG.info("Expected time left to leave: "+timeLeft+" mins. Will check uber api again at: "+bookingTime.minusMinutes(60));
            RemindTimerTask task = new RemindTimerTask(source, destination, tripStartTime, cabArrivalTime, userEmail, reportTime);
            schedule(task, bookingTime.minusMinutes(60));
        }
        else if(timeLeft>4){
            LOG.info("Expected time left to leave: "+timeLeft+" mins. Will check uber api again at: "+bookingTime.minusMinutes(timeLeft/2));
            RemindTimerTask task = new RemindTimerTask(source, destination, tripStartTime, cabArrivalTime, userEmail, reportTime);
            schedule(task, bookingTime.minusMinutes(timeLeft/2));
        }
        else{
            LOG.info("Expected time left to leave: "+timeLeft+" mins. Sending Email");
            Email email = contentService.prepareBookingReminderEmail(userEmail, source, destination, reportTime);
            reminderService.send(email);
        }
    }

    private void schedule(TimerTask task, LocalDateTime localDateTime){
        Date scheduledDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Timer timer = new Timer();
        timer.schedule(task, scheduledDate);
    }

    private long normalizeCabArrivalTime(long prevValue, long currValue){
        if(prevValue==-1)
            return currValue;

        return (long)(prevValue*DECAY_FACTOR) + (long)(currValue*(1-DECAY_FACTOR));
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setEstimateService(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    public void setReminderService(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }
    //</editor-fold>

    class RemindTimerTask extends TimerTask{
        private GeoLocation source, destination;
        private LocalDateTime tripStartTime;
        private long estimatedCabArrivalTime;
        private String user;
        private LocalDateTime reportTime;


        private RemindTimerTask(GeoLocation source, GeoLocation destination, LocalDateTime tripStartTime, long estimatedCabArrivalTime, String user, LocalDateTime reportTime) {
            this.source = source;
            this.destination = destination;
            this.tripStartTime = tripStartTime;
            this.estimatedCabArrivalTime = estimatedCabArrivalTime;
            this.user = user;
            this.reportTime = reportTime;
        }

        @Override
        public void run() {
            checkAndRemind(source, destination, tripStartTime, estimatedCabArrivalTime, user, reportTime);
        }
    }


    //Test Method
    public static void main(String[] arg){

        //Calculate Normalized value
        long val = new ReminderFacadeImpl().normalizeCabArrivalTime(10, 20);
        System.out.println("Normalized Value: "+val);
        assert val == 18;

        //Schedule
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Hello Sagar");
            }
        };
        new ReminderFacadeImpl().schedule(task, LocalDateTime.now().plusSeconds(5));
    }
}
