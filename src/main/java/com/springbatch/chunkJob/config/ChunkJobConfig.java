package com.springbatch.chunkJob.config;

import com.springbatch.chunkJob.model.StudentCsv;
import com.springbatch.chunkJob.model.StudentJdbc;
import com.springbatch.chunkJob.processor.IntegerItemProcessor;
import com.springbatch.chunkJob.reader.CsvFileItemReader;
import com.springbatch.chunkJob.reader.IntegerItemReader;
import com.springbatch.chunkJob.reader.JdbcItemReader;
import com.springbatch.chunkJob.writer.CsvFileItemWriter;
import com.springbatch.chunkJob.writer.IntegerItemWriter;
import com.springbatch.chunkJob.writer.JdbcItemWriter;
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


    //================ csv  =======================
    @Autowired
    private CsvFileItemReader csvFileItemReader;

    @Autowired
    private CsvFileItemWriter csvFileItemWriter;
    //================ csv  =======================


    //================ JDBC =======================
    @Autowired
    private JdbcItemReader jdbcItemReader;
    @Autowired
    private JdbcItemWriter jdbcItemWriter;
    //================ JDBC =======================


    public ChunkJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("Get Chunk job")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }

    private Step chunkStep() {
        return stepBuilderFactory.get("Chunk Step")
                .<Integer, Long>chunk(3)
                .reader(integerItemReader)
                .processor(integerItemProcessor)
                .writer(integerItemWriter)
                .build();
    }

    //============================== chunk and tasklet combination start ============================================

    @Bean
    public Job chunkAndTaskletJob() {
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
            System.out.println("Step Execution context: " + chunkContext.getStepContext().getStepExecutionContext());
            return RepeatStatus.FINISHED;
        };
        return tasklet;
    }
    //================================ chunk and tasklet combination end =====================================


    //=============================== CSV file Processing start ================================================

    @Bean
    public Job chunkJobForCsvFile() {
        return jobBuilderFactory.get("Get Chunk job for CSV file")
                .incrementer(new RunIdIncrementer())
                .start(chunkStepForCsvFile())
                .build();
    }

    private Step chunkStepForCsvFile() {
        return stepBuilderFactory.get("Chunk Step for CSV file")
                .<StudentCsv, StudentCsv>chunk(3)
                .reader(csvFileItemReader.flatFileItemReader())
                //.processor(integerItemProcessor)
                .writer(csvFileItemWriter)
                .build();
    }

    //=============================== CSV file Processing end ================================================

    //=============================== JDBC Processing start ==================================================
    @Bean
    public Job chunkJobForJdbc() {
        return jobBuilderFactory.get("Chunk Job for JDBC")
                .incrementer(new RunIdIncrementer())
                .start(chunkStepForJdbc())
                .build();
    }

    private Step chunkStepForJdbc() {
        return stepBuilderFactory.get("First Chunk Step")
                .<StudentJdbc, StudentJdbc>chunk(3)
                .reader(jdbcItemReader.jdbcJdbcCursorItemReader())
                .writer(jdbcItemWriter)
                .build();
    }
    //=============================== JDBC Processing end ==================================================
}
