package com.r5n.gradetrackerapi.repository;

import com.r5n.gradetrackerapi.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    /**
     * Returns the last activity of a class.
     * @param classId
     * @return
     */
    @Query(value = "SELECT * FROM activities WHERE class_id = ?1 ORDER BY pos DESC LIMIT 1;", nativeQuery = true)
    Activity findLastByClassId(UUID classId);

    /**
     * Returns all activities of a class sorted by pos attribute in ascending order.
     */
    @Query(value = "SELECT * FROM activities WHERE class_id = ?1 ORDER BY pos ASC;", nativeQuery = true)
    List<Activity> findAllByBelongingClassId(UUID classId);
}
