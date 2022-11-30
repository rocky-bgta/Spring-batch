package com.springbatch.controller;

import com.springbatch.request.JobParamsRequest;
import com.springbatch.service.JobLauncherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobRestController {

	@Autowired
	JobLauncherService jobLauncherService;

	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName,
						   @RequestBody List<JobParamsRequest> jobParamsRequestList){
		jobLauncherService.startJob(jobName, jobParamsRequestList);
		return "Job Started...";
	}
}
