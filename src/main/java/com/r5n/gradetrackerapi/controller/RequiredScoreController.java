package com.r5n.gradetrackerapi.controller;

import com.r5n.gradetrackerapi.model.Class;
import com.r5n.gradetrackerapi.payload.response.RequiredScoreResponse;
import com.r5n.gradetrackerapi.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RequiredScoreController {

    @Autowired
    ClassRepository classRepository;

    @GetMapping("/classes/{id}/required-score")
    public ResponseEntity<?> getRequiredScore(
            @PathVariable("id") UUID classId,
            @RequestParam(name = "final_grades",
                    required = false,
                    defaultValue = "50,60,70,80,90,100") List<Double> finalGrades
    ) {
        RequiredScoreResponse response = new RequiredScoreResponse(classRepository.getById(classId));

//        if (finalGrades == null) {
//            finalGrades = Arrays.asList(50.0, 60.0, 70.0, 80.0, 90.0, 100.0);
//        }

        for (Double finalGrade : finalGrades) {
            response.addTarget(finalGrade);
        }

        return ResponseEntity.ok().body(response);
    }
}
