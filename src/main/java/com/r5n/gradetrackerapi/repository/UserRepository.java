package com.r5n.gradetrackerapi.repository;

import com.r5n.gradetrackerapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
