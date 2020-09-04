package com.github.routineer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/routines")
class RoutinesController {

    @GetMapping
    List<Routine> getRoutines() {
        return List.of();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    Routine createRoutine(@RequestBody Routine routine) {
        return routine.toBuilder()
                .id("1")
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    static class Routine {
        String id;
        Schedule schedule;
        Action action;
    }

    @Data
    static class Schedule {
        String interval;
    }

    @Data
    static class Action {
        Webhook webhook;
    }

    @Data
    static class Webhook {
        String url;
    }

}
