package com.decagon.obamax.BlogRest.controller;

import com.decagon.obamax.BlogRest.model.User;
import com.decagon.obamax.BlogRest.response.ApiResponse;
import com.decagon.obamax.BlogRest.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, Model model) {
        model.addAttribute("register_user", user);
        User createdUser = userService.register(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId,
                                   @Valid @RequestBody User userRequest) {
        User updatedUser = userService.editUserDetails(userId, userRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deActivateAccount(userId);
        return new ResponseEntity<>(new ApiResponse(true, "User has been deleted."), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpSession session, @RequestBody User user) {
        Optional<User> authUser = Optional.ofNullable(userService.authenticate(user));
        if (authUser.isPresent()) {
            session.setAttribute("currentUser", authUser.get());
            return new ResponseEntity<>(new ApiResponse(true, "Login Successful."), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "Login Failed."), HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>(new ApiResponse(true, "Logout Successful."), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/")
    public ResponseEntity<?> connect(HttpSession session, @PathVariable Long userId) {
        User follower = (User) session.getAttribute("currentUser");
        return userService.makeConnection(follower, userId);
    }

    @GetMapping("/users/{userId}/followers")
    public List<User> getConnections(@PathVariable Long userId) {
        Optional<User> user = userService.getUser(userId);
        return user.get().getFollowers();
    }
}
