package com.infybuzz.service;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

@Service
public class ServiceTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("This is tasklet implementation through service");
        //Get job execution context
        System.out.println(chunkContext.getStepContext().getJobExecutionContext());

        return RepeatStatus.FINISHED;
    }
}
