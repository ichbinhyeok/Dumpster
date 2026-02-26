package com.dumpster.calculator.web.viewmodel;

import com.dumpster.calculator.infra.persistence.StoredEstimate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record ShareEstimateViewModel(
        String estimateId,
        String expiresAtText,
        StoredEstimate storedEstimate
) {

    public static ShareEstimateViewModel from(StoredEstimate storedEstimate) {
        String expiresAtText = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")
                .withZone(ZoneId.systemDefault())
                .format(storedEstimate.expiresAt());
        return new ShareEstimateViewModel(storedEstimate.estimateId(), expiresAtText, storedEstimate);
    }
}

