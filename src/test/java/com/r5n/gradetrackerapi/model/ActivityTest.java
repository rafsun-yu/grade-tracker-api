package com.r5n.gradetrackerapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    void getWeightAchieved() {
        Activity a = new Activity();
        a.setWeight(25.0);
        a.setTotal(100.0);
        a.setScore(75.0);
        assertEquals(18.75, a.getWeightAchieved(), 0.01);
    }
}