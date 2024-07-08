package com.dexter.Spring_security_client.event;

import com.dexter.Spring_security_client.entity.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {


    private AppUser appUser;
    private String applicationUrl;

    public RegistrationCompleteEvent(AppUser appUser, String applicationUrl) {
        super(appUser);
        this.appUser = appUser;
        this.applicationUrl =  applicationUrl;
    }
}
