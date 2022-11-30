package com.springbatch.service;

import com.springbatch.request.JobParamsRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobLauncherService {

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("taskletJob")
    @Autowired
    Job taskletJob;

    @Qualifier("chunkJob")
    @Autowired
    Job chunkJob;

    @Async
    public void startJob(String jobName, List<JobParamsRequest> jobParamsRequestList){
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));

        jobParamsRequestList.stream().forEach(jobParams->{
            params.put(jobParams.getParamKey(), new JobParameter(jobParams.getParamValue()));
        });

        JobParameters jobParameters = new JobParameters(params);
        try {
            JobExecution jobExecution = null;
            if(jobName.equals("tasklet")) {
               jobExecution =  jobLauncher.run(taskletJob, jobParameters);
            } else if(jobName.equals("chunk")) {
                jobExecution = jobLauncher.run(chunkJob, jobParameters);
            }
            System.out.println("JobExecution ID = " + jobExecution.getId());
        }catch (Exception ex){
            System.out.println("Exception while starting Job");
        }

    }
}