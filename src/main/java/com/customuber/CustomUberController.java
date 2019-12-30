package com.customuber;

import com.customuber.bean.GeoLocation;
import com.customuber.exception.ReminderFacadeException;
import com.customuber.facade.ReminderFacade;
import com.customuber.service.ApiLoggingService;
import com.customuber.service.ConfigurationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@RequestMapping("/")
@Controller
public class CustomUberController {


    private static final String REMIND_PAGE = "remind";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");


    @Resource(name = "reminderFacade")
    private ReminderFacade reminderFacade;

    @Resource(name = "apiLoggingService")
    private ApiLoggingService apiLoggingService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @RequestMapping(method = RequestMethod.GET)
    public String hello(ModelMap model){
        model.addAttribute("logs", apiLoggingService.getLogs());
        return REMIND_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, path="/remind")
    public String remind(ModelMap model){
        model.addAttribute("logs", apiLoggingService.getLogs());
        return REMIND_PAGE;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/remind")
    public String remind(@RequestParam(name="email") String email,
                          @RequestParam(name="source") String strSource,
                          @RequestParam(name="destination") String strDestination,
                          @RequestParam(name="time") String strReportDateTime,
                          ModelMap model) throws ParseException {

        String[] sourceArr = strSource.split(",");
        sourceArr[0] = sourceArr[0].trim();
        sourceArr[1] = sourceArr[1].trim();

        String[] destinationArr = strDestination.split(",");
        destinationArr[0] = destinationArr[0].trim();
        destinationArr[1] = destinationArr[1].trim();

        GeoLocation source = new GeoLocation(Double.parseDouble(sourceArr[0]), Double.parseDouble(sourceArr[1]));
        GeoLocation destination = new GeoLocation(Double.parseDouble(destinationArr[0]), Double.parseDouble(destinationArr[1]));

        String timeZone = configurationService.getZone().getId();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date reportDate = simpleDateFormat.parse(strReportDateTime);
        LocalDateTime reportDateTime = reportDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        try {
            reminderFacade.scheduleReminder(source, destination, reportDateTime, email);
            model.addAttribute("success", true);
        } catch (ReminderFacadeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("logs", apiLoggingService.getLogs());
        return REMIND_PAGE;
    }
}
