package com.example.dio.domain;

public record ClientRoute(String id, String company, String sourceFolder, String targetFolder, Integer cloudId, String ExtensionType, ProcessingType processingType) { }
