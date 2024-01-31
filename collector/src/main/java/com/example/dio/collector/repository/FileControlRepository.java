package com.example.dio.collector.repository;

import com.example.dio.domain.FileControl;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
public class FileControlRepository {

    private final JdbcClient jdbcClient;

    public void append(FileControl fileControl) {
        jdbcClient.sql("INSERT INTO file_control(id,trace_id,route_id,filename,copied_at) values (?,?,?,?,?)")
            .params(List.of(fileControl.id(), fileControl.traceId(), fileControl.routeId(), fileControl.filename(), fileControl.copiedAt()))
            .update();
    }

}
