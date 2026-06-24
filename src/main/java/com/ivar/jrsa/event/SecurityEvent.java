package com.ivar.jrsa.event;

public record SecurityEvent(
        SecurityEventType eventType,
        EventSeverity severity,
        String source,
        String details
) {}