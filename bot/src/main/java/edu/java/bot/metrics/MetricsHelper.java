package edu.java.bot.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetricsHelper {

    @Autowired
    private MeterRegistry meterRegistry;

    public Counter buildCounter() {
        return Counter.builder("counter").register(meterRegistry);
    }
}
