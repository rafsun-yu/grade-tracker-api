package com.r5n.gradetrackerapi.model;

public enum Term {
    FALL("Fall"),
    WINTER("Winter"),
    SUMMER("Summer")
    ;

    private final String label;

    Term(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}