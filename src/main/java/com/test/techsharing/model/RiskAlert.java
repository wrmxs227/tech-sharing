package com.test.techsharing.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RiskAlert {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private String alertId;
    private String repo;
    private String contributor;
    private String severity;
    private String reason;
    private long eventTime;

    public RiskAlert() {
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "RiskAlert{"
                + "alertId='" + alertId + '\''
                + ", repo='" + repo + '\''
                + ", contributor='" + contributor + '\''
                + ", severity='" + severity + '\''
                + ", reason='" + reason + '\''
                + ", eventTime=" + FORMATTER.format(Instant.ofEpochMilli(eventTime))
                + '}';
    }
}
