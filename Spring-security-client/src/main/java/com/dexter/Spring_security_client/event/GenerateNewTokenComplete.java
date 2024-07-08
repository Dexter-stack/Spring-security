package com.dexter.Spring_security_client.event;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.entity.VerificationToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class GenerateNewTokenComplete extends ApplicationEvent {

    private String applicationUrl;
    private VerificationToken verificationToken;
    public GenerateNewTokenComplete(VerificationToken verificationToken, String applicationUrl) {
        super(verificationToken);
        this.verificationToken = verificationToken;
        this.applicationUrl =  applicationUrl;

    }
}
