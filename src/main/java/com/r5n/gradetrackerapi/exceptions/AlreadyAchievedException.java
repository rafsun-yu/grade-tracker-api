package com.r5n.gradetrackerapi.exceptions;

public class AlreadyAchievedException extends RuntimeException {
    public AlreadyAchievedException() {
    }

    public AlreadyAchievedException(String message) {
        super(message);
    }
}
