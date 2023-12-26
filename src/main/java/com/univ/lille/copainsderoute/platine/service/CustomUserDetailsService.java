package com.univ.lille.copainsderoute.platine.service;

import com.univ.lille.copainsderoute.platine.entity.User;
import com.univ.lille.copainsderoute.platine.exceptions.UserNotFoundException;
import com.univ.lille.copainsderoute.platine.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.get().getLogin())
                        .password(user.get().getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }
}
