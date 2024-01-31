package com.example.dio.collector.service;

import com.example.dio.collector.repository.FileControlRepository;
import com.example.dio.collector.repository.ScanControlRepository;
import com.example.dio.domain.*;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Observed
@AllArgsConstructor
public class CopyFilesService {

    private final FileControlRepository fileControlRepository;
    private final ScanControlRepository scanControlRepository;

    @SneakyThrows
    @Transactional
    public void copyFile(Path origin, String destination, ClientRoute clientRoute, String traceId) {
        int lastIndex = origin.toString().lastIndexOf('/');
        var filename = origin.toString().substring(lastIndex + 1);
        var destPath = Path.of(destination.concat(filename));
        FileUtils.copyFile(origin.toFile(), destPath.toFile());
        FileControl fileControl = new FileControl(UUID.randomUUID(), traceId, clientRoute.id(), origin.toString(), LocalDateTime.now());
        fileControlRepository.append(fileControl);
        ScanControl scanControl = new ScanControl(clientRoute.id(), LocalDateTime.now());
        scanControlRepository.update(scanControl);
        log.info("Copied file={} to destination={} and registered data in scan/copy control tables", origin, destPath);
    }

    static int numberOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }

}
