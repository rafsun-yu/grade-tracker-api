package com.r5n.gradetrackerapi.model;

import com.r5n.gradetrackerapi.exceptions.AlreadyAchievedException;
import com.r5n.gradetrackerapi.exceptions.NotPossibleException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest {

    @Test
    void getRequiredWeightPercentage() {
        // Setup data.
        Class c = new Class();
        c.setActivityList(new ArrayList<>());

        Activity a1 = new Activity("Test 1", 25.0, 100.0);
        Activity a2 = new Activity("Test 2", 25.0, 100.0);
        Activity a3 = new Activity("Test 3", 25.0, 100.0);
        Activity a4 = new Activity("Test 4", 25.0, 100.0);

        a1.setScore(43.0);
        a2.setScore(47.0);
        a3.setScore(95.0);

        c.getActivityList().add(a1);
        c.getActivityList().add(a2);
        c.getActivityList().add(a3);
        c.getActivityList().add(a4);

        // Test not possible
        assertThrows(NotPossibleException.class, () -> c.getRequiredWeightPercentage(72.0));

        // Test already achieved
        assertThrows(AlreadyAchievedException.class, () -> c.getRequiredWeightPercentage(46.0));

        // Test possible
        assertEquals(75.0, c.getRequiredWeightPercentage(65.0), 0.01);

        // Test no more remaining weight
        a4.setScore(100.0);
        assertThrows(NotPossibleException.class, () -> c.getRequiredWeightPercentage(46.0));
    }
}