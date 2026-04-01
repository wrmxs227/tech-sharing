package com.test.techsharing.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.techsharing.model.ContributionEvent;
import com.test.techsharing.model.EventType;
import com.test.techsharing.util.DemoConfig;
import com.test.techsharing.util.JsonMapperFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class OpenSourceContributionMockProducer {

    private static final ObjectMapper OBJECT_MAPPER = JsonMapperFactory.create();
    private static final Random RANDOM = new Random();
    private static final List<String> REPOSITORIES = List.of(
            "flink-metrics-lab",
            "stream-governance-hub",
            "opensource-risk-radar",
            "realtime-community-insight",
            "commit-velocity-board"
    );
    private static final Map<String, String> REPO_ORG = Map.of(
            "flink-metrics-lab", "ApacheLab",
            "stream-governance-hub", "DataFlowers",
            "opensource-risk-radar", "InfraPulse",
            "realtime-community-insight", "OpenWave",
            "commit-velocity-board", "MergeCraft"
    );
    private static final List<String> CONTRIBUTORS = List.of(
            "linan",
            "qiaoyu",
            "liushu",
            "zhangqi",
            "chenyi",
            "sunlei",
            "mengfan",
            "hejing"
    );
    private static final List<String> LANGUAGES = List.of("Java", "Scala", "Python", "Go");

    public static void main(String[] args) throws Exception {
        Properties demoProperties = DemoConfig.load();
        int eventsPerSecond = Integer.parseInt(demoProperties.getProperty("demo.producer.events-per-second", "6"));
        int maxEvents = Integer.parseInt(demoProperties.getProperty("demo.producer.max-events", "0"));

        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, demoProperties.getProperty("demo.kafka.bootstrap.servers"));
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.ACKS_CONFIG, "1");
        kafkaProperties.put(ProducerConfig.LINGER_MS_CONFIG, "50");

        long sleepMillis = Math.max(1000L / Math.max(1, eventsPerSecond), 100L);
        String topic = demoProperties.getProperty("demo.kafka.topic");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties)) {
            int sent = 0;
            while (maxEvents <= 0 || sent < maxEvents) {
                // 造数逻辑既要足够稳定，方便演示时复现，
                // 也要保留一定随机性，让不同仓库和贡献者的输出更真实。
                ContributionEvent event = nextEvent();
                String payload = OBJECT_MAPPER.writeValueAsString(event);
                producer.send(new ProducerRecord<>(topic, event.getRepo(), payload));
                sent++;

                if (sent % Math.max(1, eventsPerSecond) == 0) {
                    System.out.println("Produced " + sent + " events, latest=" + payload);
                }
                Thread.sleep(sleepMillis);
            }
        }
    }

    private static ContributionEvent nextEvent() {
        String repo = pick(REPOSITORIES);
        EventType eventType = pickEventType();
        int linesAdded = linesAdded(eventType);
        int linesDeleted = linesDeleted(eventType);
        int filesChanged = filesChanged(eventType);
        int reviewLatencySeconds = reviewLatencySeconds(eventType);
        double qualityScore = qualityScore(eventType);

        ContributionEvent event = new ContributionEvent();
        event.setEventId("evt-" + UUID.randomUUID().toString().substring(0, 8));
        event.setRepo(repo);
        event.setContributor(pick(CONTRIBUTORS));
        event.setOrganization(REPO_ORG.get(repo));
        event.setLanguage(pick(LANGUAGES));
        event.setEventType(eventType);
        event.setLinesAdded(linesAdded);
        event.setLinesDeleted(linesDeleted);
        event.setFilesChanged(filesChanged);
        event.setReviewLatencySeconds(reviewLatencySeconds);
        event.setQualityScore(qualityScore);
        event.setEventTime(System.currentTimeMillis() - RANDOM.nextInt(4000));
        return event;
    }

    private static EventType pickEventType() {
        int score = RANDOM.nextInt(100);
        if (score < 28) {
            return EventType.CODE_PUSHED;
        }
        if (score < 46) {
            return EventType.PR_OPENED;
        }
        if (score < 60) {
            return EventType.PR_MERGED;
        }
        if (score < 72) {
            return EventType.REVIEW_SUBMITTED;
        }
        if (score < 84) {
            return EventType.ISSUE_OPENED;
        }
        if (score < 94) {
            return EventType.ISSUE_CLOSED;
        }
        return EventType.DOCS_UPDATED;
    }

    private static int linesAdded(EventType eventType) {
        return switch (eventType) {
            case CODE_PUSHED -> 80 + RANDOM.nextInt(320);
            case PR_OPENED -> 120 + RANDOM.nextInt(900);
            case PR_MERGED -> 90 + RANDOM.nextInt(700);
            case REVIEW_SUBMITTED -> 0;
            case ISSUE_OPENED, ISSUE_CLOSED -> 0;
            case DOCS_UPDATED -> 20 + RANDOM.nextInt(120);
        };
    }

    private static int linesDeleted(EventType eventType) {
        return switch (eventType) {
            case CODE_PUSHED -> 10 + RANDOM.nextInt(180);
            case PR_OPENED -> RANDOM.nextInt(450);
            case PR_MERGED -> RANDOM.nextInt(900);
            case REVIEW_SUBMITTED -> 0;
            case ISSUE_OPENED, ISSUE_CLOSED -> 0;
            case DOCS_UPDATED -> RANDOM.nextInt(40);
        };
    }

    private static int filesChanged(EventType eventType) {
        return switch (eventType) {
            case CODE_PUSHED -> 2 + RANDOM.nextInt(9);
            case PR_OPENED -> 3 + RANDOM.nextInt(45);
            case PR_MERGED -> 2 + RANDOM.nextInt(30);
            case REVIEW_SUBMITTED -> 1 + RANDOM.nextInt(8);
            case ISSUE_OPENED, ISSUE_CLOSED -> 1;
            case DOCS_UPDATED -> 1 + RANDOM.nextInt(5);
        };
    }

    private static int reviewLatencySeconds(EventType eventType) {
        return switch (eventType) {
            case PR_MERGED -> 600 + RANDOM.nextInt(9600);
            case REVIEW_SUBMITTED -> 120 + RANDOM.nextInt(2400);
            default -> 0;
        };
    }

    private static double qualityScore(EventType eventType) {
        double base = switch (eventType) {
            case PR_OPENED -> 48 + RANDOM.nextDouble() * 40;
            case PR_MERGED -> 62 + RANDOM.nextDouble() * 33;
            case CODE_PUSHED -> 55 + RANDOM.nextDouble() * 35;
            case REVIEW_SUBMITTED -> 70 + RANDOM.nextDouble() * 25;
            case ISSUE_OPENED, ISSUE_CLOSED -> 75 + RANDOM.nextDouble() * 20;
            case DOCS_UPDATED -> 80 + RANDOM.nextDouble() * 18;
        };
        return Math.round(base * 10.0) / 10.0;
    }

    private static <T> T pick(List<T> values) {
        return values.get(RANDOM.nextInt(values.size()));
    }
}
