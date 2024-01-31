package com.example.dio.collector.service;

import com.example.dio.collector.repository.ScanControlRepository;
import com.example.dio.domain.ClientRoute;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@Observed
@AllArgsConstructor
public class ScanRouteService {

    private final ScanControlRepository scanControlRepository;

    /**
     * List all files in the clientRoute source folder that were created after the given instant
     * @param clientRoute the clientRoute to list the files
     * @return the list of files
     */
    public List<Path> listFiles(ClientRoute clientRoute) {
        return listFiles(clientRoute, lastUpdatedInstantForRouteId(clientRoute.id()));
    }

    public Instant lastUpdatedInstantForRouteId(String routeId) {
        var localDateTime = scanControlRepository.lastUpdatedDateForRouteId(routeId);
        Instant beginningOfDay = Instant.now().truncatedTo(ChronoUnit.DAYS);
        if (localDateTime.isEmpty()) {
            return beginningOfDay;
        } else {
            ZoneId defaultZone = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = localDateTime.get().atZone(defaultZone);
            return zonedDateTime.toInstant();
        }
    }

    /**
     * List all files in the clientRoute source folder that were created after the given instant
     * @param clientRoute the clientRoute to list the files
     * @param fromInstant the instant to compare the file creation time
     * @return the list of files
     */
    @SneakyThrows
    private List<Path> listFiles(ClientRoute clientRoute, Instant fromInstant) {
        log.info("Listing files in {} created after {}", clientRoute.sourceFolder(), fromInstant);
        try (var stream = Files.list(Path.of(clientRoute.sourceFolder()))) {
            return stream
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(clientRoute.ExtensionType()))
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toInstant().isAfter(fromInstant);
                    } catch (IOException e) {
                        log.error("Error listing path={}", path);
                        return false;
                    }
                }).toList();
        }
    }

}
