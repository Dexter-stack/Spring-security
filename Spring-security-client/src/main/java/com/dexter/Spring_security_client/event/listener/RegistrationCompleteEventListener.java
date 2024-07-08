package com.dexter.Spring_security_client.event.listener;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.event.RegistrationCompleteEvent;
import com.dexter.Spring_security_client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@Slf4j

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        // create the verification token for the appUser with link

        AppUser appUser = event.getAppUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationToken(token, appUser);


        // send link to appUser mail

//        sendVerificationEmail()
        String url =  event.getApplicationUrl()+"/verifyRegistration?token="+token;
        log.info("Click the link to verify your account: {} ", url);



    }
}
