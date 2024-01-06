package com.univ.lille.copainsderoute.platine.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univ.lille.copainsderoute.platine.entity.UserToken;
@Repository
public interface UserTokenRepository  extends JpaRepository<UserToken, Integer>{

    Optional<UserToken> findByToken(String token);
    
}
