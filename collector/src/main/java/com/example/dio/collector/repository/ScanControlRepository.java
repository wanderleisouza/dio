package com.example.dio.collector.repository;

import com.example.dio.domain.ScanControl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class ScanControlRepository {

    private final JdbcClient jdbcClient;

    public Optional<LocalDateTime> lastUpdatedDateForRouteId(String routeId) {
        return jdbcClient.sql("SELECT scanned_at FROM scan_control WHERE route_id = ?")
            .param(routeId)
            .query(LocalDateTime.class)
            .optional();
    }

    @Transactional
    public void update(ScanControl scanControl) {

        LocalDateTime beginOfCurrentDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        Optional<LocalDateTime> lastUpdate = jdbcClient.sql("""
                 SELECT max(scanned_at) FROM scan_control
                 WHERE (route_id = ? AND scanned_at > ?)
                """)
            .params(List.of(scanControl.routeId(), beginOfCurrentDay))
            .query(LocalDateTime.class).optional();

        if (lastUpdate.isEmpty()) {
                jdbcClient.sql("INSERT INTO scan_control (route_id, scanned_at) values (?, ?) on conflict (route_id) do nothing")
                    .params(List.of(scanControl.routeId(), scanControl.scannedAt()))
                    .update();
        } else if (lastUpdate.get().isBefore(scanControl.scannedAt())) {
             log.info("Updating latest scanned value for the routeId={} from {} to {} ",
                 scanControl.routeId(), lastUpdate, scanControl.scannedAt());

            jdbcClient.sql("UPDATE scan_control SET scanned_at = ? WHERE route_id = ?")
                .params(List.of(scanControl.scannedAt(), scanControl.routeId()))
                .update();
        }

    }

}
