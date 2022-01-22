package com.r5n.gradetrackerapi.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.*;
import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "classes")
public class Class {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(name = "course_name")
    @JsonProperty("course_name")
    private String courseName;

    @NotNull
    @Column(name = "section")
    private Character section;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "term")
    private Term term;

    @NotNull
    @Column(name = "year")
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User enrolledUser;

    @OneToMany(mappedBy = "belongingClass")
    @JsonProperty("activities")
    private List<Activity> activityList;

    public Class() {
    }

    public Class(String courseName, Character section, Term term, Integer year, User enrolledUser) {
        this.courseName = courseName;
        this.section = section;
        this.term = term;
        this.year = year;
        this.enrolledUser = enrolledUser;
    }
}