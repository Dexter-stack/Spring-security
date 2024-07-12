package com.dexter.Spring_security_client.serviceImpl;

import com.dexter.Spring_security_client.entity.AppUser;
import com.dexter.Spring_security_client.entity.PasswordResetToken;
import com.dexter.Spring_security_client.entity.Role;
import com.dexter.Spring_security_client.entity.VerificationToken;
import com.dexter.Spring_security_client.model.UserModel;
import com.dexter.Spring_security_client.repository.PasswordResetTokenRepository;
import com.dexter.Spring_security_client.repository.UserRepository;
import com.dexter.Spring_security_client.repository.VerificationTokenRepository;
import com.dexter.Spring_security_client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public AppUser registerUser(UserModel userModel) {
        AppUser appUser = new AppUser();
        appUser.setEmail(userModel.getEmail());
        appUser.setFirstName(userModel.getFirstName());
        appUser.setLastName(userModel.getLastName());
        appUser.setRole(Role.USER);

        //encrypt pasword before saving

        appUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(appUser);


        return appUser;

    }

    @Override
    public void saveVerificationToken(String token, AppUser appUser) {

        VerificationToken verificationToken =  new VerificationToken(appUser, token);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null){
            return  "invalid";
        }
        AppUser user = verificationToken.getAppUser();
        Calendar calendar =  Calendar.getInstance();

        if(verificationToken.getExpirationTime().getTime()-calendar.getTime().getTime()<=0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";

        }

        user.setEnabled(true);
        userRepository.save(user);
        return  "valid";
    }

    @Override
    public VerificationToken generateNewVrificationToken(String oldToken) {
        VerificationToken verificationToken =  verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;

    }

    @Override
    public AppUser findUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }



    @Override
    public void createPasswordResetTokenForUser(AppUser user, String token) {

        PasswordResetToken passwordResetToken =  new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);

    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null){
            return  "invalid";
        }
        AppUser user = passwordResetToken.getAppUser();
        Calendar calendar =  Calendar.getInstance();

        if(passwordResetToken.getExpirationTime().getTime()-calendar.getTime().getTime()<=0){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";

        }


        return  "valid";
    }

    @Override
    public Optional<AppUser> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getAppUser());
    }

    @Override
    public void changeUserPassword(AppUser appUser, String newPassword) {
        appUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(appUser);
    }

    @Override
    public boolean checkIfValidOldPassword(AppUser user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public Optional <AppUser> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}
