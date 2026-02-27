package com.dumpster.calculator.infra.tracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrackingCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(TrackingCleanupJob.class);

    private final TrackingService trackingService;
    private final int retentionDays;

    public TrackingCleanupJob(
            TrackingService trackingService,
            @Value("${app.tracking.retention-days:90}") int retentionDays
    ) {
        this.trackingService = trackingService;
        this.retentionDays = Math.max(1, retentionDays);
    }

    @Scheduled(cron = "0 15 2 * * *")
    public void deleteExpiredEvents() {
        int deleted = trackingService.deleteOlderThanDays(retentionDays);
        if (deleted > 0) {
            log.info("Deleted expired tracking events: {}", deleted);
        }
    }
}
