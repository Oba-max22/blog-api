package com.decagon.obamax.BlogRest.repository;

import com.decagon.obamax.BlogRest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
//    List<User> findAllByUserDeactivated(int i);

    List<User> findAllByIsDeactivated(int i);
}
