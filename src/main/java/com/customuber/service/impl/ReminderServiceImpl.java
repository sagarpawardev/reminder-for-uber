package com.customuber.service.impl;

import com.customuber.bean.Email;
import com.customuber.service.ApiLoggingService;
import com.customuber.service.ConfigurationService;
import com.customuber.service.ReminderService;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import static com.customuber.constant.Constants.*;

public class ReminderServiceImpl implements ReminderService {

    private ConfigurationService configurationService;
    private ApiLoggingService apiLoggingService;

    @Override
    public void send(Email uberEmail) {

        String smtpServer = configurationService.getProperty(KEY_SMTP_SERVER);
        int smtpPort = Integer.parseInt(configurationService.getProperty(KEY_SMTP_PORT));
        String smtpUserName = configurationService.getProperty(KEY_SMTP_USERNAME);
        String smtpPassword = configurationService.getProperty(KEY_SMTP_PASSWORD);
        String fromEmail = configurationService.getProperty(KEY_FROM_EMAIL);

        try {
            org.apache.commons.mail.Email email = new SimpleEmail();
            email.setHostName(smtpServer);
            email.setSmtpPort(smtpPort);
            email.setAuthenticator(new DefaultAuthenticator(smtpUserName, smtpPassword));
            email.setSSL(true);
            email.setFrom(fromEmail);
            email.setSubject(uberEmail.getSubject());
            email.setMsg(uberEmail.getBody());
            email.addTo(uberEmail.getTo());
            email.send();

        } catch (EmailException e) {
            e.printStackTrace();
        }


        apiLoggingService.log("email", uberEmail.getTo());
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setApiLoggingService(ApiLoggingService apiLoggingService) {
        this.apiLoggingService = apiLoggingService;
    }

}
