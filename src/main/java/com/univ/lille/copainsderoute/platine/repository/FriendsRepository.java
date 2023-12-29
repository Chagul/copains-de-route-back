package com.univ.lille.copainsderoute.platine.repository;

import com.univ.lille.copainsderoute.platine.entity.Friends;
import com.univ.lille.copainsderoute.platine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Integer> {

    @Query("SELECT f FROM Friends f WHERE f.sender = :requester OR f.added = :requester AND f.status = 'SENT'")
    Optional<List<Friends>> findBySenderOrAdded(@Param("requester") User requester);

    Optional<Friends> findBySenderAndAdded(User sender, User added);
}
