package com.dexter.Spring_security_client.service;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.model.UserModel;

public interface UserService {

    public AppUser registerUser(UserModel userModel);

    void saveVerificationToken(String token, AppUser appUser);

    String validateVerificationToken(String token);
}
