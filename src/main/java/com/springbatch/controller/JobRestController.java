package com.springbatch.controller;

import com.springbatch.request.JobParamsRequest;
import com.springbatch.service.JobLauncherService;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobRestController {

	@Autowired
	JobLauncherService jobLauncherService;

	@Autowired
	JobOperator jobOperator;

	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName,
						   @RequestBody List<JobParamsRequest> jobParamsRequestList){
		jobLauncherService.startJob(jobName, jobParamsRequestList);
		return "Job Started...";
	}

	@GetMapping("/stop/{jobExecutionId}")
	public String stopJob(@PathVariable long jobExecutionId){
		try {
			jobOperator.stop(jobExecutionId);
		} catch (NoSuchJobExecutionException | JobExecutionNotRunningException  e) {
			e.printStackTrace();
		}
		return "Job Stopped...";
	}
}
