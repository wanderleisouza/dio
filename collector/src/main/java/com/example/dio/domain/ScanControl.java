package com.example.dio.domain;

import java.time.LocalDateTime;

public record ScanControl(String routeId, LocalDateTime scannedAt) { }
