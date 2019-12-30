package com.customuber.service.impl;

import com.customuber.service.ApiLoggingService;
import com.customuber.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ApiLoggingServiceImpl implements ApiLoggingService {

    private static Logger LOG = LoggerFactory.getLogger(ApiLoggingService.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh : mm a");

    private ConfigurationService configurationService;

    private List<String> logList = new LinkedList<>();

    public ApiLoggingServiceImpl() {
        initializeIfRequired();
    }

    private void initializeIfRequired(){
        if(logList.size()==0){
            URL url = this.getClass().getClassLoader().getResource("api_logs.txt");

            String filePath = "";
            if(url != null){
                filePath = url.getPath();
            }

            File file = new File(filePath);
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);
                reader.lines().forEach( line -> logList.add(line));
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void log(String api, String user) {

        ZoneId zoneId = this.configurationService.getZone();
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        zonedDateTime = zonedDateTime.withZoneSameInstant(configurationService.getZone());
        String time = zonedDateTime.format(formatter);

        String message = "["+ time +"] - Requested "+api+" api for ["+user+"]\n";
        LOG.info(message);
        logList.add(0, message);

        try {
            URL url = this.getClass().getClassLoader().getResource("api_logs.txt");
            String filePath = "";
            if(url != null){
                filePath = url.getPath();
            }
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public List<String> getLogs() {
        return Collections.unmodifiableList(logList);
    }


}
