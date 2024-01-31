package com.example.dio.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileControl(UUID id, String traceId, String routeId, String filename, LocalDateTime copiedAt) {}
