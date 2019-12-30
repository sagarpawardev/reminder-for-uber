package com.customuber.service;

import com.customuber.bean.Email;

public interface ReminderService {

    /**
     * Sends email
     * @param email email to send
     */
    void send(Email email);

}
