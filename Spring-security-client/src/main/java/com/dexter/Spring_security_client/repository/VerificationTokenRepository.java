package com.dexter.Spring_security_client.repository;

import com.dexter.Spring_security_client.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken ,Long> {
    VerificationToken findByToken(String token );
}
