package com.dexter.Spring_security_client.controller;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.entity.VerificationToken;
import com.dexter.Spring_security_client.event.GenerateNewTokenComplete;
import com.dexter.Spring_security_client.event.RegistrationCompleteEvent;
import com.dexter.Spring_security_client.model.LoginRequest;
import com.dexter.Spring_security_client.model.PasswordModel;
import com.dexter.Spring_security_client.model.UserModel;
import com.dexter.Spring_security_client.service.UserService;
import com.dexter.Spring_security_client.serviceImpl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private JwtService jwtService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


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
        return "Verification token sent";

    }



    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){


        AppUser  user =  userService.findUserByEmail(passwordModel.getEmail());

        String url = "";
        if(user != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user,applicationUrl(request),token);



        }
        return url;


    }


    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return  "Invalid Token";
        }
        Optional<AppUser> user =  userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
         userService.changeUserPassword(user.get(),passwordModel.getNewPassword());
        }else{
            return "Invalid token";
        }
    return  "password updated successfully";

    }


    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        AppUser user = userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.checkIfValidOldPassword(user,passwordModel.getOldPassword())){
            return "invalid old password";
        }

        //save new password
        userService.changeUserPassword(user,passwordModel.getNewPassword());

        return "password save successfully";
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(loginRequest);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

    }




        private String passwordResetTokenMail(AppUser user, String applicationUrl,String token){
        String url =  applicationUrl+"/savePassword?token="+token;


        // sendMail
        log.info("Click the link to Reset  your password: {} ", url);
      return  url;
    }



    private String applicationUrl(HttpServletRequest request) {
        return  "http://"+ request.getServerName()+":"+request.getServerPort()+request.getContextPath();

    }

}
