package com.dexter.Spring_security_client.controller;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.entity.VerificationToken;
import com.dexter.Spring_security_client.event.GenerateNewTokenComplete;
import com.dexter.Spring_security_client.event.RegistrationCompleteEvent;
import com.dexter.Spring_security_client.model.UserModel;
import com.dexter.Spring_security_client.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;


    @GetMapping("/hello")
    public String hello(){
        return "Hello Dexter";
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){

        AppUser appUser = userService.registerUser(userModel);
        // separate out the process from this flow
        // create event to publish and pass it
        publisher.publishEvent(new RegistrationCompleteEvent(appUser,applicationUrl(request)));


   return "Success";
    }


    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verified Successfuly";
        }
        return "Bad User";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerification(@RequestParam("token") String oldToken, HttpServletRequest httpServletRequest){
        VerificationToken verificationToken = userService.generateNewVrificationToken(oldToken);
        AppUser user = verificationToken.getAppUser();

        publisher.publishEvent(new GenerateNewTokenComplete(verificationToken,applicationUrl(httpServletRequest)));
//        resendVerificationTokenMail(user, applicationUrl(httpServletRequest));
        return "Verification token sent";

    }

    private void resendVerificationTokenMail(AppUser user, String s) {


    }

    private String applicationUrl(HttpServletRequest request) {
        return  "http://"+ request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
