package com.r5n.gradetrackerapi.model;

import com.r5n.gradetrackerapi.exceptions.AlreadyAchievedException;
import com.r5n.gradetrackerapi.exceptions.NotPossibleException;
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

    /**
     * Returns the percentage of weight required in each remaining
     * activities to achieve the provided final grade.
     * @param finalGrade Must be between 0 and 100.
     * @throws NotPossibleException No more remaining weight, or remaining weight is not sufficient.
     * @throws AlreadyAchievedException Provided final grade is already achieved.
     */
    public Double getRequiredWeightPercentage(Double finalGrade) {

        if (getRemainingWeight().equals(0.0))
            throw new NotPossibleException("No more weight remaining.");

        Double requiredScore = (finalGrade - getTotalAchievedWeight()) / getRemainingWeight();

        if (Double.compare(requiredScore, 0.0) < 0)
            throw new AlreadyAchievedException(String.format("The score %f is already achieved.", finalGrade));

        if (Double.compare(requiredScore, 1.0) > 0)
            throw new NotPossibleException(String.format("The remaining weight is not sufficient to achieve %f.", finalGrade));

        return requiredScore * 100;
    }

    /**
     * Returns the total weight scored cumulatively in all activities.
     */
    private Double getTotalAchievedWeight() {
       return this.activityList.stream()
               .filter(Activity::isGraded)
               .mapToDouble(Activity::getWeightAchieved)
               .sum();
    }

    /**
     * Returns the cumulative weight of all ungraded activities.
     */
    private Double getRemainingWeight() {
        return this.activityList.stream()
                .filter(a -> !a.isGraded())
                .mapToDouble(Activity::getWeight)
                .sum();
    }
}