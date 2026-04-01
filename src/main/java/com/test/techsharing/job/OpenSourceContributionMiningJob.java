package com.test.techsharing.job;

import com.test.techsharing.model.ContributionEvent;
import com.test.techsharing.util.DemoConfig;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;
import java.util.Properties;

public class OpenSourceContributionMiningJob {

    public static void main(String[] args) throws Exception {
        Properties properties = DemoConfig.load();
        int parallelism = Integer.parseInt(properties.getProperty("demo.flink.parallelism", "1"));
        int repoWindowSeconds = Integer.parseInt(properties.getProperty("demo.window.repo.seconds", "60"));
        int contributorWindowSeconds = Integer.parseInt(properties.getProperty("demo.window.contributor.seconds", "30"));
        int watermarkSeconds = Integer.parseInt(properties.getProperty("demo.watermark.seconds", "5"));

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parallelism);

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(properties.getProperty("demo.kafka.bootstrap.servers"))
                .setTopics(properties.getProperty("demo.kafka.topic"))
                .setGroupId(properties.getProperty("demo.kafka.group-id"))
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        WatermarkStrategy<ContributionEvent> watermarkStrategy = WatermarkStrategy
                .<ContributionEvent>forBoundedOutOfOrderness(Duration.ofSeconds(watermarkSeconds))
                .withTimestampAssigner((event, timestamp) -> event.getEventTime());

        // 同一条 Kafka 事件流会被拆分成多个分析视角，
        // 方便在演示中展示 Flink 如何复用同一数据源完成不同挖掘任务。
        DataStream<ContributionEvent> events = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "opensource-kafka-source")
                .map(new ContributionEventJsonDeserializer())
                .name("json-to-contribution-event")
                .assignTimestampsAndWatermarks(watermarkStrategy)
                .name("event-watermarks");

        events.keyBy(ContributionEvent::getRepo)
                .window(TumblingEventTimeWindows.of(Time.seconds(repoWindowSeconds)))
                .process(new RepoActivityWindowFunction())
                .name("repo-activity-window")
                .print("repo-heat");

        events.keyBy(ContributionEvent::getContributor)
                .window(TumblingEventTimeWindows.of(Time.seconds(contributorWindowSeconds)))
                .process(new ContributorImpactWindowFunction())
                .name("contributor-impact-window")
                .print("contributor-impact");

        events.filter(new RiskyContributionFilter())
                .map(new RiskAlertMapper())
                .name("risk-alerts")
                .print("risk-alert");

        env.execute("Open Source Contribution Mining Demo");
    }
}
