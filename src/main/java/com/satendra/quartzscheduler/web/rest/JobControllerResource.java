package com.satendra.quartzscheduler.web.rest;

import com.satendra.quartzscheduler.model.JobDescriptor;
import com.satendra.quartzscheduler.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class JobControllerResource {

    private final JobService jobService;


    @PostMapping(path = "/groups/{group}/jobs")
    public ResponseEntity<JobDescriptor> createJob(@PathVariable String group, @Valid @RequestBody JobDescriptor descriptor) {
        return new ResponseEntity<>(jobService.createJob(group, descriptor), CREATED);
    }


    @GetMapping(path = "/groups/{group}/jobs/{name}")
    public ResponseEntity<JobDescriptor> findJob(@PathVariable String group, @PathVariable String name) {
        return jobService.findJob(group, name).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping(path = "/groups/{group}/jobs/{name}")
    public ResponseEntity<Void> updateJob(@PathVariable String group, @PathVariable String name, @RequestBody JobDescriptor descriptor) {
        jobService.updateJob(group, name, descriptor);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(path = "/groups/{group}/jobs/{name}")
    public ResponseEntity<Void> deleteJob(@PathVariable String group, @PathVariable String name) {
        jobService.deleteJob(group, name);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping(path = "/groups/{group}/jobs/{name}/pause")
    public ResponseEntity<Void> pauseJob(@PathVariable String group, @PathVariable String name) {
        jobService.pauseJob(group, name);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping(path = "/groups/{group}/jobs/{name}/resume")
    public ResponseEntity<Void> resumeJob(@PathVariable String group, @PathVariable String name) {
        jobService.resumeJob(group, name);
        return ResponseEntity.noContent().build();
    }
}
