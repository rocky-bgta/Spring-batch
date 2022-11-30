package com.infybuzz.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job")
public class JobRestController {
	
	@Autowired
	JobLauncher jobLauncher;

	@Qualifier("taskletJob")
	@Autowired
	Job taskletJob;

	@Qualifier("chunkJob")
	@Autowired
	Job chunkJob;

	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName) throws Exception {
		
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));
		
		JobParameters jobParameters = new JobParameters(params);
		
		if(jobName.equals("tasklet")) {
			jobLauncher.run(taskletJob, jobParameters);
		} else if(jobName.equals("chunk")) {
			jobLauncher.run(chunkJob, jobParameters);
		}
		
		return "Job Started...";
	}
}
