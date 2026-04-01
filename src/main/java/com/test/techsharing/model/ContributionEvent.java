package com.test.techsharing.model;

public class ContributionEvent {

    private String eventId;
    private String repo;
    private String contributor;
    private String organization;
    private String language;
    private EventType eventType;
    private int linesAdded;
    private int linesDeleted;
    private int filesChanged;
    private int reviewLatencySeconds;
    private double qualityScore;
    private long eventTime;

    public ContributionEvent() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public int getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    public int getLinesDeleted() {
        return linesDeleted;
    }

    public void setLinesDeleted(int linesDeleted) {
        this.linesDeleted = linesDeleted;
    }

    public int getFilesChanged() {
        return filesChanged;
    }

    public void setFilesChanged(int filesChanged) {
        this.filesChanged = filesChanged;
    }

    public int getReviewLatencySeconds() {
        return reviewLatencySeconds;
    }

    public void setReviewLatencySeconds(int reviewLatencySeconds) {
        this.reviewLatencySeconds = reviewLatencySeconds;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "ContributionEvent{"
                + "eventId='" + eventId + '\''
                + ", repo='" + repo + '\''
                + ", contributor='" + contributor + '\''
                + ", organization='" + organization + '\''
                + ", language='" + language + '\''
                + ", eventType=" + eventType
                + ", linesAdded=" + linesAdded
                + ", linesDeleted=" + linesDeleted
                + ", filesChanged=" + filesChanged
                + ", reviewLatencySeconds=" + reviewLatencySeconds
                + ", qualityScore=" + qualityScore
                + ", eventTime=" + eventTime
                + '}';
    }
}
