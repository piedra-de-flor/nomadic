package com.example.Triple_clone.configuration;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxConfig {

    @Value("${management.influx.metrics.export.token}")
    private String token;

    @Value("${management.influx.metrics.export.org}")
    private String org;

    @Value("${management.influx.metrics.export.bucket}")
    private String bucket;

    @Value("${management.influx.metrics.export.uri}")
    private String url;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}
