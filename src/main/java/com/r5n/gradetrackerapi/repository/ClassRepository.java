package com.r5n.gradetrackerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.r5n.gradetrackerapi.model.Class;

import java.util.*;

public interface ClassRepository extends JpaRepository<Class, UUID> {
    /**
     * Returns a list of classes that belongs to the provided user.
     * @param userId ID of the user.
     * @return List<Class>
     */
    List<Class> findByEnrolledUserId(String userId);
}
