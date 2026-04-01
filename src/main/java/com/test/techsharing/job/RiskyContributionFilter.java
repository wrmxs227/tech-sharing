package com.test.techsharing.job;

import com.test.techsharing.model.ContributionEvent;
import com.test.techsharing.model.EventType;
import org.apache.flink.api.common.functions.FilterFunction;

public class RiskyContributionFilter implements FilterFunction<ContributionEvent> {

    @Override
    public boolean filter(ContributionEvent event) {
        // 告警规则保持尽量直观：
        // 大而低质的 PR、长时间评审后才合并的 PR、删改异常偏重的提交都会被立即标记。
        boolean oversizedLowQualityPr = event.getEventType() == EventType.PR_OPENED
                && event.getFilesChanged() >= 35
                && event.getQualityScore() < 60.0;
        boolean delayedMerge = event.getEventType() == EventType.PR_MERGED
                && event.getReviewLatencySeconds() >= 7200;
        boolean destructiveChange = event.getLinesDeleted() >= 900
                && event.getLinesAdded() <= 200;
        return oversizedLowQualityPr || delayedMerge || destructiveChange;
    }
}
