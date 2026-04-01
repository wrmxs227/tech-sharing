package com.test.techsharing.job;

import com.test.techsharing.model.ContributionEvent;
import com.test.techsharing.model.EventType;
import com.test.techsharing.model.RepoActivityMetric;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.HashSet;
import java.util.Set;

public class RepoActivityWindowFunction
        extends ProcessWindowFunction<ContributionEvent, RepoActivityMetric, String, TimeWindow> {

    @Override
    public void process(
            String repo,
            Context context,
            Iterable<ContributionEvent> elements,
            Collector<RepoActivityMetric> out) {
        // 将单个仓库在一个窗口内的事件汇总成一份“热度快照”，
        // 便于在分享时直接解释仓库活跃度是如何计算出来的。
        long totalEvents = 0L;
        long mergedPullRequests = 0L;
        long codeChurn = 0L;
        double qualityScoreSum = 0.0;
        double reviewLatencySum = 0.0;
        long reviewSamples = 0L;
        String organization = "Unknown";
        Set<String> contributors = new HashSet<>();

        for (ContributionEvent event : elements) {
            totalEvents++;
            mergedPullRequests += event.getEventType() == EventType.PR_MERGED ? 1 : 0;
            codeChurn += (long) event.getLinesAdded() + event.getLinesDeleted();
            qualityScoreSum += event.getQualityScore();
            if (event.getReviewLatencySeconds() > 0) {
                reviewLatencySum += event.getReviewLatencySeconds();
                reviewSamples++;
            }
            contributors.add(event.getContributor());
            organization = event.getOrganization();
        }

        double averageQualityScore = totalEvents == 0 ? 0.0 : qualityScoreSum / totalEvents;
        double averageReviewLatency = reviewSamples == 0 ? 0.0 : reviewLatencySum / reviewSamples;
        // 这里故意采用易讲解的加权公式，
        // 让听众能直观看到事件量、贡献者广度、合并产出和质量分之间的关系。
        double hotnessScore = totalEvents * 1.2
                + contributors.size() * 3.0
                + mergedPullRequests * 6.0
                + codeChurn / 200.0
                + averageQualityScore / 10.0
                - averageReviewLatency / 120.0;

        RepoActivityMetric metric = new RepoActivityMetric();
        metric.setRepo(repo);
        metric.setOrganization(organization);
        metric.setWindowStart(context.window().getStart());
        metric.setWindowEnd(context.window().getEnd());
        metric.setTotalEvents(totalEvents);
        metric.setMergedPullRequests(mergedPullRequests);
        metric.setActiveContributors(contributors.size());
        metric.setCodeChurn(codeChurn);
        metric.setAverageQualityScore(averageQualityScore);
        metric.setAverageReviewLatencySeconds(averageReviewLatency);
        metric.setHotnessScore(hotnessScore);
        out.collect(metric);
    }
}
