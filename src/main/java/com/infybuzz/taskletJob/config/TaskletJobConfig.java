package com.infybuzz.taskletJob.config;

import com.infybuzz.taskletJob.listener.JobListener;
import com.infybuzz.taskletJob.listener.StepListener;
import com.infybuzz.taskletJob.service.ServiceTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class TaskletJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ServiceTasklet serviceTasklet;

    @Autowired
    private JobListener jobListener;

    @Autowired
    private StepListener stepListener;

    public TaskletJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job taskletJob() {
        return jobBuilderFactory.get("First Job")
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .listener(jobListener)
                .build();
    }

    private Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(firstTask())
                .listener(stepListener)
                .build();
    }

    //Second step use Tasklet implementation for Service
    private Step secondStep() {
        return stepBuilderFactory.get("Second Step")
                .tasklet(serviceTasklet)
                .build();
    }

    private Tasklet firstTask() {
        Tasklet tasklet;
        tasklet = (stepContribution, chunkContext) -> {
            System.out.println("This is first tasklet step");
            System.out.println("Step Execution context: "+ chunkContext.getStepContext().getStepExecutionContext());
            return RepeatStatus.FINISHED;
        };
        return tasklet;
    }

   /* private Tasklet secondTask() {
        Tasklet tasklet;
        tasklet = (stepContribution, chunkContext) -> {
            System.out.println("This is second tasklet step");
            return RepeatStatus.FINISHED;
        };
        return tasklet;
    }*/
}
