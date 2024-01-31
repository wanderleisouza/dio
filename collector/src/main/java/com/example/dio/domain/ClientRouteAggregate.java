package com.example.dio.domain;

import java.nio.file.Path;
import java.util.List;

public record ClientRouteAggregate(ClientRoute clientRoute, List<Path> files) {
}
