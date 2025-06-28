package com.example.Triple_clone.common;

import com.example.Triple_clone.common.LogMessage;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricToInfluxJob {
    private static final double PERCENTAGE = 100.0;
    private final InfluxDBClient influxDBClient;

    @Scheduled(fixedRate = 300_000)
    public void reportMetrics() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();

        double cpuUsage = getCpuUsage(processor);
        double memoryUsage = getMemoryUsage(memory);

        Point point = Point
                .measurement("server_metrics")
                .addTag("host", "triple-app")
                .addField("cpu_usage", cpuUsage)
                .addField("memory_usage", memoryUsage)
                .time(Instant.now(), WritePrecision.MS);

        try (WriteApi writeApi = influxDBClient.makeWriteApi()) {
            writeApi.writePoint(point);
            log.info(LogMessage.BATCH_PROCESS_SUCCESS.format("InfluxDB write"));
        } catch (Exception e) {
            log.error(LogMessage.BATCH_PROCESS_FAIL.format("InfluxDB write", e));
        }

    }

    private double getCpuUsage(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        return processor.getSystemCpuLoadBetweenTicks(prevTicks) * PERCENTAGE;
    }

    private double getMemoryUsage(GlobalMemory memory) {
        long total = memory.getTotal();
        long used = total - memory.getAvailable();
        return (double) used / total * PERCENTAGE;
    }
}
