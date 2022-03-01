package com.r5n.gradetrackerapi.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.r5n.gradetrackerapi.exceptions.AlreadyAchievedException;
import com.r5n.gradetrackerapi.exceptions.NotPossibleException;
import com.r5n.gradetrackerapi.model.Class;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
public class RequiredScoreResponse {

    @JsonIgnore
    private Class cls;

    @JsonProperty("class_id")
    public UUID getClassId() {
        return this.cls.getId();
    }

    @JsonProperty("required_scores")
    private List<RequiredScore> requiredScores = new ArrayList<>();

    public RequiredScoreResponse() {
    }

    public RequiredScoreResponse(Class cls) {
        this.cls = cls;
    }

    public RequiredScoreResponse(Class c, Double finalGrade) {
        this(c);
        this.addTarget(finalGrade);
    }

    /**
     * Adds a target final grade and required percentage for it in the response.
     */
    public void addTarget(double finalGrade) {
        try {
            Double requiredPercentage = this.cls.getRequiredWeightPercentage(finalGrade);
            requiredScores.add(new RequiredScore(finalGrade, requiredPercentage));
        } catch (NotPossibleException ex) {
            requiredScores.add(new RequiredScore(finalGrade, ExceptionReason.NOT_POSSIBLE));
        } catch (AlreadyAchievedException ex) {
            requiredScores.add(new RequiredScore(finalGrade, ExceptionReason.ALREADY_ACHIEVED));
        } finally {
            this.requiredScores.sort(Comparator.comparingDouble(RequiredScore::getTarget));
        }
    }

    @Data
    public static class RequiredScore {
        private Double target;

        @JsonProperty("required_percentage")
        private Double requiredPercentage;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ExceptionReason exception;

        public RequiredScore(Double target, Double requiredPercentage) {
            this.target = target;
            this.requiredPercentage = requiredPercentage;
        }

        public RequiredScore(Double target, ExceptionReason exception) {
            this.target = target;
            this.exception = exception;
        }
    }

    public static enum ExceptionReason {
        NOT_POSSIBLE,
        ALREADY_ACHIEVED
    }
}
