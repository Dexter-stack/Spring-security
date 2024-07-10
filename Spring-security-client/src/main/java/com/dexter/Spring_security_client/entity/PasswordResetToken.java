package com.dexter.Spring_security_client.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION_TIME =  10;

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long Id;
    private String token;
    private Date expirationTime;

    @OneToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN")
    )
    private AppUser appUser;


    public PasswordResetToken(AppUser appUser, String token){
        this.appUser = appUser;
        this.token =  token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public PasswordResetToken (String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calendar =  Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}