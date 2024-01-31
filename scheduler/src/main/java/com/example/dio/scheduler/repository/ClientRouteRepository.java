package com.example.dio.scheduler.repository;

import com.example.dio.domain.ClientRoute;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ClientRouteRepository {

    private final JdbcClient jdbcClient;


    public List<ClientRoute> findAll() {
        return jdbcClient.sql("""
                SELECT id, company, source_folder, target_folder, cloud_id, extension_type, processing_type
                FROM   client_route
                WHERE  active = true
            """).query(ClientRoute.class).list();
    }

}
