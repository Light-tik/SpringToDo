package com.emobile.springtodo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class TodoMetricsService {

    private final Counter completedCounter;

    public TodoMetricsService(MeterRegistry meterRegistry) {
        this.completedCounter = meterRegistry.counter("todo.completed.count");
    }

    public void incrementCompleted() {
        completedCounter.increment();
    }
}
