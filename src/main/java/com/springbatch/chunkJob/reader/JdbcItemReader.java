package com.springbatch.chunkJob.reader;

import com.springbatch.chunkJob.model.StudentJdbc;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcItemReader {

    @Autowired
    private DataSource dataSource;

    public JdbcCursorItemReader<StudentJdbc> jdbcJdbcCursorItemReader() {
        JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<>();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql(
                "select id, first_name as firstName, last_name as lastName,email from student"
        );

        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<StudentJdbc>() {
            {
                setMappedClass(StudentJdbc.class);
            }
        });

        //for skip first two row
        //jdbcCursorItemReader.setCurrentItemCount(2);

        //for get maximum number of row
        //jdbcCursorItemReader.setMaxItemCount(8);


        return jdbcCursorItemReader;
    }
}


