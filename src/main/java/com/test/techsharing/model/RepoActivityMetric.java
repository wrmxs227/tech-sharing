package com.test.techsharing.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RepoActivityMetric {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private String repo;
    private String organization;
    private long windowStart;
    private long windowEnd;
    private long totalEvents;
    private long mergedPullRequests;
    private long activeContributors;
    private long codeChurn;
    private double averageQualityScore;
    private double averageReviewLatencySeconds;
    private double hotnessScore;

    public RepoActivityMetric() {
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getMergedPullRequests() {
        return mergedPullRequests;
    }

    public void setMergedPullRequests(long mergedPullRequests) {
        this.mergedPullRequests = mergedPullRequests;
    }

    public long getActiveContributors() {
        return activeContributors;
    }

    public void setActiveContributors(long activeContributors) {
        this.activeContributors = activeContributors;
    }

    public long getCodeChurn() {
        return codeChurn;
    }

    public void setCodeChurn(long codeChurn) {
        this.codeChurn = codeChurn;
    }

    public double getAverageQualityScore() {
        return averageQualityScore;
    }

    public void setAverageQualityScore(double averageQualityScore) {
        this.averageQualityScore = averageQualityScore;
    }

    public double getAverageReviewLatencySeconds() {
        return averageReviewLatencySeconds;
    }

    public void setAverageReviewLatencySeconds(double averageReviewLatencySeconds) {
        this.averageReviewLatencySeconds = averageReviewLatencySeconds;
    }

    public double getHotnessScore() {
        return hotnessScore;
    }

    public void setHotnessScore(double hotnessScore) {
        this.hotnessScore = hotnessScore;
    }

    @Override
    public String toString() {
        return "RepoActivityMetric{"
                + "repo='" + repo + '\''
                + ", organization='" + organization + '\''
                + ", window=" + FORMATTER.format(Instant.ofEpochMilli(windowStart))
                + " -> " + FORMATTER.format(Instant.ofEpochMilli(windowEnd))
                + ", totalEvents=" + totalEvents
                + ", mergedPullRequests=" + mergedPullRequests
                + ", activeContributors=" + activeContributors
                + ", codeChurn=" + codeChurn
                + ", averageQualityScore=" + String.format("%.2f", averageQualityScore)
                + ", averageReviewLatencySeconds=" + String.format("%.2f", averageReviewLatencySeconds)
                + ", hotnessScore=" + String.format("%.2f", hotnessScore)
                + '}';
    }
}
