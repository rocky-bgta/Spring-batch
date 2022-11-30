package com.infybuzz.controller;

import com.infybuzz.service.JobLauncherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job")
public class JobRestController {

	@Autowired
	JobLauncherService jobLauncherService;

	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName){
		jobLauncherService.startJob(jobName);
		return "Job Started...";
	}
}
