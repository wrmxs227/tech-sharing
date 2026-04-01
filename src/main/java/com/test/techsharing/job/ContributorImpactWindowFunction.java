package com.test.techsharing.job;

import com.test.techsharing.model.ContributionEvent;
import com.test.techsharing.model.ContributorImpactMetric;
import com.test.techsharing.model.EventType;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.HashSet;
import java.util.Set;

public class ContributorImpactWindowFunction
        extends ProcessWindowFunction<ContributionEvent, ContributorImpactMetric, String, TimeWindow> {

    @Override
    public void process(
            String contributor,
            Context context,
            Iterable<ContributionEvent> elements,
            Collector<ContributorImpactMetric> out) {
        // 将原始协作事件压缩成贡献者画像，
        // 方便实时排名展示，也方便演讲时解释指标含义。
        long eventCount = 0L;
        long mergedPullRequests = 0L;
        long closedIssues = 0L;
        long totalLinesChanged = 0L;
        double qualityScoreSum = 0.0;
        Set<String> repositoriesTouched = new HashSet<>();

        for (ContributionEvent event : elements) {
            eventCount++;
            if (event.getEventType() == EventType.PR_MERGED) {
                mergedPullRequests++;
            }
            if (event.getEventType() == EventType.ISSUE_CLOSED) {
                closedIssues++;
            }
            totalLinesChanged += (long) event.getLinesAdded() + event.getLinesDeleted();
            qualityScoreSum += event.getQualityScore();
            repositoriesTouched.add(event.getRepo());
        }

        double averageQuality = eventCount == 0 ? 0.0 : qualityScoreSum / eventCount;
        double impactScore = eventCount * 1.5
                + repositoriesTouched.size() * 2.0
                + mergedPullRequests * 7.0
                + closedIssues * 2.5
                + totalLinesChanged / 150.0
                + averageQuality / 12.0;

        ContributorImpactMetric metric = new ContributorImpactMetric();
        metric.setContributor(contributor);
        metric.setWindowStart(context.window().getStart());
        metric.setWindowEnd(context.window().getEnd());
        metric.setEventCount(eventCount);
        metric.setRepositoriesTouched(repositoriesTouched.size());
        metric.setMergedPullRequests(mergedPullRequests);
        metric.setClosedIssues(closedIssues);
        metric.setTotalLinesChanged(totalLinesChanged);
        metric.setImpactScore(impactScore);
        out.collect(metric);
    }
}
