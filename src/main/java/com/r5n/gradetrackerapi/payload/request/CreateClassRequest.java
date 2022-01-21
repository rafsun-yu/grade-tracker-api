package com.r5n.gradetrackerapi.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.r5n.gradetrackerapi.model.Term;
import lombok.Data;

@Data
public class CreateClassRequest {
    @JsonProperty("course_name")
    private String courseName;

    private Character section;

    private Term term;

    private Integer year;

    public CreateClassRequest() {
    }

    public CreateClassRequest(String courseName, Character section, Term term, Integer year) {
        this.courseName = courseName;
        this.section = section;
        this.term = term;
        this.year = year;
    }
}
