package com.infybuzz.chunkJob.config;

import com.infybuzz.chunkJob.processor.IntegerItemProcessor;
import com.infybuzz.chunkJob.reader.IntegerItemReader;
import com.infybuzz.chunkJob.writer.IntegerItemWriter;
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

@Configuration
public class ChunkJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;


    @Autowired
    private IntegerItemReader integerItemReader;

    @Autowired
    private IntegerItemProcessor integerItemProcessor;

    @Autowired
    private IntegerItemWriter integerItemWriter;


    public ChunkJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job chunkJob(){
        return jobBuilderFactory.get("Get Chunk job")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }

    private Step chunkStep(){
        return stepBuilderFactory.get("Chunk Step")
                .<Integer,Long>chunk(3)
                .reader(integerItemReader)
                .processor(integerItemProcessor)
                .writer(integerItemWriter)
                .build();
    }

    //============================== chunk and tasklet combination ============================================

    @Bean
    public Job chunkAndTaskletJob(){
        return jobBuilderFactory.get("Get Chunk and Tasklet job")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .next(taskletStep())
                .build();
    }

    private Step taskletStep() {
        return stepBuilderFactory.get("Tasklet Step")
                .tasklet(taskletWork())
                .build();
    }

    private Tasklet taskletWork() {
        Tasklet tasklet;
        tasklet = (stepContribution, chunkContext) -> {
            System.out.println("This is tasklet step");
            System.out.println("Step Execution context: "+ chunkContext.getStepContext().getStepExecutionContext());
            return RepeatStatus.FINISHED;
        };
        return tasklet;
    }
    //================================ chunk and tasklet combination =====================================

}
