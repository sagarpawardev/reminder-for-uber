package com.customuber.service.impl;

import com.customuber.constant.Constants;
import com.customuber.service.ConfigurationService;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Properties;

public class ConfigurationServiceImpl implements ConfigurationService{

    private static Properties properties;
    @Override
    public String getProperty(String key) {
        try {
            if (properties == null) {
                properties = new Properties();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
                properties.load(inputStream);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        return properties.getProperty(key);
    }

    @Override
    public ZoneId getZone() {
        String timeZone = getProperty(Constants.KEY_TIMEZONE);
        return ZoneId.of(timeZone);
    }


}
