package com.dexter.Spring_security_client.service;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.entity.VerificationToken;
import com.dexter.Spring_security_client.model.UserModel;

import java.util.Optional;

public interface UserService {

    public AppUser registerUser(UserModel userModel);

    void saveVerificationToken(String token, AppUser appUser);

    String validateVerificationToken(String token);

    VerificationToken generateNewVrificationToken(String oldToken);

    AppUser findUserByEmail(String email);

    void createPasswordResetTokenForUser(AppUser user, String token);

    String validatePasswordResetToken(String token);

    Optional<AppUser> getUserByPasswordResetToken(String token);

    void changeUserPassword(AppUser appUser, String newPassword);

    boolean checkIfValidOldPassword(AppUser user, String oldPassword);

    Optional<AppUser> findByEmail(String email);
}
