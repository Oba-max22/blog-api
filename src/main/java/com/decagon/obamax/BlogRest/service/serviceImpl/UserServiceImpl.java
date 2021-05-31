package com.decagon.obamax.BlogRest.service.serviceImpl;

import com.decagon.obamax.BlogRest.exception.ResourceNotFoundException;
import com.decagon.obamax.BlogRest.model.CommentLikes;
import com.decagon.obamax.BlogRest.model.User;
import com.decagon.obamax.BlogRest.repository.UserRepository;
import com.decagon.obamax.BlogRest.response.ApiResponse;
import com.decagon.obamax.BlogRest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user;
    }

    @Override
    public User register(User user) {
        return userRepository.save(user);
    }

    @Override
    public User editUserDetails(Long userId, User userRequest) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            user.get().setFirstName(userRequest.getFirstName());
            user.get().setLastName(userRequest.getLastName());
            user.get().setEmail(userRequest.getEmail());
            user.get().setPassword(userRequest.getPassword());
            return userRepository.save(user.get());
        } else {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
    }

    @Override
    public void deActivateAccount(Long userId) {
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 5);
            String presentDate = DateFor.format(calendar.getTime());
            user.get().setIsDeactivated(1);
            user.get().setDeleteDate(presentDate);
            userRepository.save(user.get());
        } else {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
    }

    @Override
    public User authenticate(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public void deactivatedUserScheduler() {
        List<User> users = userRepository.findAllByIsDeactivated(1);
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        System.out.println("scheduler working");
        users.forEach(user -> {
            String presentDate = DateFor.format(date);
            String deleteDate = user.getDeleteDate();
            int actionDelete = presentDate.compareTo(deleteDate);
            if (actionDelete >= 0) {
                user.setIsDeleted(1);
                userRepository.save(user);
            }
        });
    }

    @Override
    public ResponseEntity<?> makeConnection(User follower, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<User> userFollowers = user.get().getFollowers();
            userFollowers.add(follower);
            user.get().setFollowers(userFollowers);
            user.get().setId(userId);
            userRepository.save(user.get());
            return new ResponseEntity<>(new ApiResponse(true, "Connection made successfully"), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("User not found with userId " + userId);
        }
    }

    public void checkUserExists (Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
    }
}
