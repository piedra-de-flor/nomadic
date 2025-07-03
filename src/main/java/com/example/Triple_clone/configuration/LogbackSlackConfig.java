package com.example.Triple_clone.configuration;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.Triple_clone.domain.notification.infra.ChannelNotificationSender;
import com.example.Triple_clone.common.logging.SlackLogbackAppender;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackSlackConfig {
    private final ChannelNotificationSender slackNotificationSender;

    public LogbackSlackConfig(ChannelNotificationSender slackNotificationSender) {
        this.slackNotificationSender = slackNotificationSender;
    }

    @PostConstruct
    public void init() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);

        SlackLogbackAppender slackAppender = new SlackLogbackAppender();
        slackAppender.setSlackSender(slackNotificationSender);
        slackAppender.setContext(context);
        slackAppender.start();

        rootLogger.addAppender(slackAppender);
    }
}

