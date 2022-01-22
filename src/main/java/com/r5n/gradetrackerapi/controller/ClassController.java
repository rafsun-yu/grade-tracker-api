package com.r5n.gradetrackerapi.controller;

import com.r5n.gradetrackerapi.model.Activity;
import com.r5n.gradetrackerapi.model.Class;
import com.r5n.gradetrackerapi.model.User;
import com.r5n.gradetrackerapi.repository.*;
import com.r5n.gradetrackerapi.payload.request.CreateClassRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ClassController {

    @Autowired
    ClassRepository classRepository;

    @PostMapping("/classes")
    public ResponseEntity<?> createClass(@RequestBody CreateClassRequest createClassRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Class cls = new Class(
                createClassRequest.getCourseName(),
                createClassRequest.getSection(),
                createClassRequest.getTerm(),
                createClassRequest.getYear(),
                user
        );
        Class savedClass = classRepository.save(cls);
        return  ResponseEntity.created(null).body(savedClass);
    }

    @GetMapping("/classes")
    public ResponseEntity<?> getClasses() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Class> classes = classRepository.findByEnrolledUserId(user.getId());

        // Sorts activities by proper order.
        for (Class c: classes) {
            c.getActivityList().sort(Comparator.comparing(Activity::getOrder));
        }

        return ResponseEntity.ok().body(classes);
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<?> removeClass(@PathVariable("id") UUID classId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Class cls = classRepository.getById(classId);

        // Checks whether the class belongs to the user.
        if (!cls.getEnrolledUser().equals(loggedInUser))
            throw new AccessDeniedException("You don't have access to the resource.");

        classRepository.deleteById(classId);
        return ResponseEntity.noContent().build();
    }
}
