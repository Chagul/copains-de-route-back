package com.univ.lille.copainsderoute.platine.repository;

import com.univ.lille.copainsderoute.platine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String userLogin);
    void deleteByLogin(String userLogin);
}
