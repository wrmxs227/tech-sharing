param(
    [string]$FlinkHome = "D:\flink-1.17.2"
)

$env:JAVA_HOME = "D:\Program Files\java17.0.12"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

& "$FlinkHome\bin\flink.bat" run -c com.test.techsharing.job.OpenSourceContributionMiningJob ".\target\tech-sharing-1.0.0.jar"
