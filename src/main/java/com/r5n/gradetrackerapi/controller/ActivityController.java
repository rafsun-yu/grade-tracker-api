package com.r5n.gradetrackerapi.controller;

import com.google.common.collect.Iterables;
import com.r5n.gradetrackerapi.model.Activity;
import com.r5n.gradetrackerapi.model.Class;
import com.r5n.gradetrackerapi.model.User;
import com.r5n.gradetrackerapi.repository.ActivityRepository;
import com.r5n.gradetrackerapi.repository.ClassRepository;
import com.r5n.gradetrackerapi.utils.NullAwareBeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ActivityController {

    @Autowired
    ClassRepository classRepository;

    @Autowired
    ActivityRepository activityRepository;

    @PostMapping("/classes/{id}/activities")
    public ResponseEntity<?> createActivity(
            @PathVariable("id") UUID classId,
            @RequestBody Activity activity
    ) {
        Class cls = getClassFromUUID(classId);
        Integer order = 0;

        // Sets order to the end.
        if (cls.getActivityList().size() != 0)
            order = activityRepository.findLastByClassId(cls.getId()).getOrder() + 1;

        activity.setOrder(order);
        activity.setBelongingClass(cls);
        Activity savedActivity = activityRepository.save(activity);
        return  ResponseEntity.created(null).body(savedActivity);
    }

    @PatchMapping("/classes/{id}/activities/ordering")
    public ResponseEntity<?> orderActivities(
            @PathVariable("id") UUID classId,
            @RequestBody List<UUID> activityIds
    ) {
        List<Activity> allActivities = activityRepository.findAllByBelongingClassId(classId);

        // To-be sorted activity list.
        List<Activity> sortActivities = activityIds.stream()
                .map((id) -> activityRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .collect(Collectors.toCollection(ArrayList::new));

        // Loops through to-be sorted activities starting from the end.
        Collections.reverse(sortActivities);
        for (Activity a : sortActivities) {
            // Shifts 'a' to the beginning of the actual list.
            allActivities.remove(a);
            allActivities.add(0, a);
        }

        // Updates 'pos' attribute.
        Integer currentPos = 0;
        for (Activity a : allActivities) {
            a.setOrder(currentPos);
            activityRepository.save(a);
            currentPos++;
        }

        return ResponseEntity.ok().body(allActivities);
    }

    @PatchMapping("/activities/{id}")
    public ResponseEntity<?> updateActivity(
            @PathVariable("id") UUID activityId,
            @RequestBody Activity patchActivity
    ) throws InvocationTargetException, IllegalAccessException {
        Activity dbActivity = activityRepository.findById(activityId).orElse(null);

        if (dbActivity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "activity not found");

        NullAwareBeanUtilsBean notNull = new NullAwareBeanUtilsBean();
        notNull.copyProperties(dbActivity, patchActivity);
        dbActivity = activityRepository.save(dbActivity);

        return ResponseEntity.ok().body(dbActivity);
    }

    @DeleteMapping("/activities/{id}")
    public ResponseEntity<?> removeActivity(@PathVariable("id") UUID activityId) {
        activityRepository.deleteById(activityId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returns an instance of Class from UUID, if the instance belongs to the authorized user.
     * Otherwise, throws AccessDeniedException.
     * @param uuid UUID of class.
     * @return An instance of class.
     */
    private Class getClassFromUUID(UUID uuid) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Class cls = classRepository.getById(uuid);

        // Checks whether the class belongs to the user.
        if (!cls.getEnrolledUser().equals(loggedInUser))
            throw new AccessDeniedException("You don't have access to the resource.");

        return cls;
    }
}
