package com.decagon.obamax.BlogRest.service;

import com.decagon.obamax.BlogRest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserService {
    Page<User> getAllUsers(Pageable pageable);
    Optional<User> getUser(Long userId);
    User register(User user);
    User editUserDetails(Long userId, User userRequest);
    void deActivateAccount(Long userId);
    User authenticate(User user);
    void deactivatedUserScheduler();
    ResponseEntity<?> makeConnection(User follower, Long userId);
}
