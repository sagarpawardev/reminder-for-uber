package com.customuber.service;

import java.util.List;

public interface ApiLoggingService {

    /**
     * Logs Api Calls
     * @param api name of the api
     * @param user user id
     */
    void log(String api, String user);

    /**
     * Fetches list of api call logs
     * @return list of logs
     */
    List<String> getLogs();
}
