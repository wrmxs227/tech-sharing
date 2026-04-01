package com.test.techsharing.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ContributorImpactMetric {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private String contributor;
    private long windowStart;
    private long windowEnd;
    private long eventCount;
    private long repositoriesTouched;
    private long mergedPullRequests;
    private long closedIssues;
    private long totalLinesChanged;
    private double impactScore;

    public ContributorImpactMetric() {
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public long getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(long windowStart) {
        this.windowStart = windowStart;
    }

    public long getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(long windowEnd) {
        this.windowEnd = windowEnd;
    }

    public long getEventCount() {
        return eventCount;
    }

    public void setEventCount(long eventCount) {
        this.eventCount = eventCount;
    }

    public long getRepositoriesTouched() {
        return repositoriesTouched;
    }

    public void setRepositoriesTouched(long repositoriesTouched) {
        this.repositoriesTouched = repositoriesTouched;
    }

    public long getMergedPullRequests() {
        return mergedPullRequests;
    }

    public void setMergedPullRequests(long mergedPullRequests) {
        this.mergedPullRequests = mergedPullRequests;
    }

    public long getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(long closedIssues) {
        this.closedIssues = closedIssues;
    }

    public long getTotalLinesChanged() {
        return totalLinesChanged;
    }

    public void setTotalLinesChanged(long totalLinesChanged) {
        this.totalLinesChanged = totalLinesChanged;
    }

    public double getImpactScore() {
        return impactScore;
    }

    public void setImpactScore(double impactScore) {
        this.impactScore = impactScore;
    }

    @Override
    public String toString() {
        return "ContributorImpactMetric{"
                + "contributor='" + contributor + '\''
                + ", window=" + FORMATTER.format(Instant.ofEpochMilli(windowStart))
                + " -> " + FORMATTER.format(Instant.ofEpochMilli(windowEnd))
                + ", eventCount=" + eventCount
                + ", repositoriesTouched=" + repositoriesTouched
                + ", mergedPullRequests=" + mergedPullRequests
                + ", closedIssues=" + closedIssues
                + ", totalLinesChanged=" + totalLinesChanged
                + ", impactScore=" + String.format("%.2f", impactScore)
                + '}';
    }
}
