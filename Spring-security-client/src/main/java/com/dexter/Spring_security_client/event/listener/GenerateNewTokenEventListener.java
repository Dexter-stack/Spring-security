package com.dexter.Spring_security_client.event.listener;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.entity.VerificationToken;
import com.dexter.Spring_security_client.event.GenerateNewTokenComplete;
import com.dexter.Spring_security_client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@Slf4j
public class GenerateNewTokenEventListener implements ApplicationListener<GenerateNewTokenComplete> {


    @Override
    public void onApplicationEvent(GenerateNewTokenComplete event) {

        VerificationToken verificationToken=  event.getVerificationToken();
       // sendMail()

        String token = verificationToken.getToken();
        String url =  event.getApplicationUrl()+"/verifyRegistration?token="+token;
        log.info("Click the link to verify your account: {} ", url);

    }
}
