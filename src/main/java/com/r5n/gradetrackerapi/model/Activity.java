package com.r5n.gradetrackerapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "weight")
    private Double weight;

    @Column(name = "total")
    private Double total;

    @Column(name = "score")
    private Double score;

    @Column(name = "pos")
    @JsonIgnore
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @JsonIgnore
    private Class belongingClass;

    public Activity() {
    }

    public Activity(String name, Double weight, Double total) {
        this.name = name;
        this.weight = weight;
        this.total = total;
    }

    /**
     * Returns the weight scored.
     */
    public Double getWeightAchieved() {
        return this.score / this.total * this.weight;
    }

    /**
     * Returns whether the activity is graded.
     * @return
     */
    public boolean isGraded() {
        return this.score != null;
    }
}
