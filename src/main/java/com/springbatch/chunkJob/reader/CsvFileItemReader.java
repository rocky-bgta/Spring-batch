package com.springbatch.chunkJob.reader;

import com.springbatch.chunkJob.model.StudentCsv;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CsvFileItemReader {

    public FlatFileItemReader<StudentCsv> flatFileItemReader() {
        FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<>();
        File file = new File("C:\\GitRepo\\Spring-batch\\inputFiles\\students.csv");
        flatFileItemReader.setResource(new FileSystemResource(file));
//
//        flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
//            {
//                setLineTokenizer(new DelimitedLineTokenizer() {
//                    {
//                        setNames("ID", "First Name", "Last Name", "Email");
//                        setDelimiter(",");
//                    }
//                });
//
//                setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
//                    {
//                        setTargetType(StudentCsv.class);
//                    }
//                });
//            }
//        });

        DefaultLineMapper<StudentCsv> defaultLineMapper =
                new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");
        delimitedLineTokenizer.setDelimiter(",");

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        BeanWrapperFieldSetMapper<StudentCsv> fieldSetMapper =
                new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StudentCsv.class);

        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);


        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }
}


