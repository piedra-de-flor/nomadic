package com.example.Triple_clone.service.monitoring;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class MetricToInfluxJob {
    private final InfluxDBClient influxDBClient;

    @Scheduled(fixedRate = 300000)
    public void reportMetrics() {
        double memoryUsage = getMemoryUsage();
        double cpuUsage = getCpuUsage();

        Point point = Point
                .measurement("server_metrics")
                .addTag("host", "triple-app")
                .addField("memory_usage", memoryUsage)
                .addField("cpu_usage", cpuUsage)
                .time(Instant.now(), WritePrecision.MS);

        influxDBClient.getWriteApi().writePoint(point);
    }

    private double getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return (double) (runtime.totalMemory() - runtime.freeMemory()) / runtime.maxMemory();
    }

    private double getCpuUsage() {
        return 0.5;
    }
}
