package com.dumpster.calculator.infra.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EstimateCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(EstimateCleanupJob.class);

    private final EstimateStorageService estimateStorageService;

    public EstimateCleanupJob(EstimateStorageService estimateStorageService) {
        this.estimateStorageService = estimateStorageService;
    }

    @Scheduled(cron = "0 15 * * * *")
    public void deleteExpiredEstimates() {
        int deleted = estimateStorageService.deleteExpired();
        if (deleted > 0) {
            log.info("Deleted expired estimates: {}", deleted);
        }
    }
}

