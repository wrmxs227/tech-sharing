package com.test.techsharing.job;

import com.test.techsharing.model.ContributionEvent;
import com.test.techsharing.model.EventType;
import com.test.techsharing.model.RiskAlert;
import org.apache.flink.api.common.functions.MapFunction;

public class RiskAlertMapper implements MapFunction<ContributionEvent, RiskAlert> {

    @Override
    public RiskAlert map(ContributionEvent event) {
        RiskAlert alert = new RiskAlert();
        alert.setAlertId("alert-" + event.getEventId());
        alert.setRepo(event.getRepo());
        alert.setContributor(event.getContributor());
        alert.setSeverity(resolveSeverity(event));
        alert.setReason(resolveReason(event));
        alert.setEventTime(event.getEventTime());
        return alert;
    }

    private String resolveSeverity(ContributionEvent event) {
        if (event.getLinesDeleted() >= 1200) {
            return "HIGH";
        }
        if (event.getEventType() == EventType.PR_MERGED && event.getReviewLatencySeconds() >= 7200) {
            return "MEDIUM";
        }
        return "MEDIUM";
    }

    private String resolveReason(ContributionEvent event) {
        if (event.getEventType() == EventType.PR_OPENED
                && event.getFilesChanged() >= 35
                && event.getQualityScore() < 60.0) {
            return "Large PR with low quality score";
        }
        if (event.getEventType() == EventType.PR_MERGED && event.getReviewLatencySeconds() >= 7200) {
            return "PR merged after a long review cycle";
        }
        return "High deletion volume with limited replacement code";
    }
}
