package com.customuber.service;

import java.time.ZoneId;

public interface ConfigurationService {

    /**
     * Returns value of passed key and null if property not found.
     * @param key property
     * @return value of property
     */
    String getProperty(String key);

    /**
     * Finds configured ZoneId
     * @return zoneId
     */
    ZoneId getZone();
}
