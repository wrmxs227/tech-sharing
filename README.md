# Flink Open Source Contribution Mining Demo

这是一个用于技术分享演示的小型 Flink 项目，主题是“开源协作事件的实时挖掘”。

项目假设团队把 GitHub / GitLab 的协作动作投递到 Kafka，Flink 对这些事件做实时计算，输出三类结果：

- `repo-heat`：一分钟窗口内最活跃的仓库画像
- `contributor-impact`：三十秒窗口内贡献者影响力快照
- `risk-alert`：高风险改动和异常协作行为告警

## 业务背景

为了贴合“开源贡献技术分析会”，这里把事件流建模成以下开源协作行为：

- `CODE_PUSHED`
- `PR_OPENED`
- `PR_MERGED`
- `ISSUE_OPENED`
- `ISSUE_CLOSED`
- `REVIEW_SUBMITTED`
- `DOCS_UPDATED`

## Demo 架构

本地可运行版本默认使用：

- Kafka: `localhost:9092`
- Topic: `opensource.contrib.events.v1`
- Flink: 本机 Flink 1.17
- JDK: 17

分享时可以把它描述成一套更接近生产的架构：

- Kafka Brokers: `10.20.0.21:9092,10.20.0.22:9092,10.20.0.23:9092`
- Flink JobManager: `10.20.1.10`
- Flink TaskManagers: `10.20.1.11-12`
- 事件源: GitHub Webhook / CI Pipeline / Repo Mirror

## 事件格式

Kafka 中每条消息都是一条 JSON，格式如下：

```json
{
  "eventId": "evt-7e7bd2f2",
  "repo": "flink-metrics-lab",
  "contributor": "linan",
  "organization": "ApacheLab",
  "language": "Java",
  "eventType": "PR_MERGED",
  "linesAdded": 420,
  "linesDeleted": 105,
  "filesChanged": 17,
  "reviewLatencySeconds": 1860,
  "qualityScore": 87.5,
  "eventTime": 1761962105000
}
```

## 运行前准备

1. 把 `JAVA_HOME` 指到 JDK 17  
   Windows PowerShell 示例：

```powershell
$env:JAVA_HOME='D:\Program Files\java17.0.12'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

2. 确保 Kafka 已启动，并创建 Topic：

```powershell
kafka-topics.bat --create `
  --topic opensource.contrib.events.v1 `
  --bootstrap-server localhost:9092 `
  --partitions 3 `
  --replication-factor 1
```

3. 可选：根据你的本机环境修改 [demo.properties](D:/陕西交控/Code/tech-sharing/src/main/resources/demo.properties)

## 构建

```powershell
mvn -DskipTests package
```

构建完成后会生成：

- `target/tech-sharing-1.0.0.jar`：最终提交给 Flink 的可运行 jar
- `target/original-tech-sharing-1.0.0.jar`：未打包依赖的原始 jar

## 启动顺序

1. 启动事件模拟器

```powershell
mvn exec:java "-Dexec.mainClass=com.test.techsharing.producer.OpenSourceContributionMockProducer"
```

2. 提交 Flink 任务

```powershell
flink run -c com.test.techsharing.job.OpenSourceContributionMiningJob .\target\tech-sharing-1.0.0.jar
```

如果 `flink` 没有加入 `PATH`，可以直接使用你本地 Flink 安装目录下的 `flink.bat`。

## 演示亮点

- 主题贴合开源协作和技术贡献
- 事件结构清晰，方便讲 Kafka Topic 设计
- 同一个流拆成多个分析视角，适合讲 Flink DataStream
- 有窗口聚合、水位线、风险过滤、实时控制台输出

## 建议的演示顺序

1. 先展示 Kafka Topic 中的 JSON 事件
2. 再启动 Producer，持续制造协作事件
3. 提交 Flink Job，观察三类输出流
4. 强调实时计算如何帮助社区运营、代码治理和贡献者分析
