package com.example.sweter.repos;

import com.example.sweter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findById(int id);
    void deleteUserById(int id);

}
