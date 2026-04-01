$env:JAVA_HOME = "D:\Program Files\java17.0.12"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

mvn exec:java "-Dexec.mainClass=com.test.techsharing.producer.OpenSourceContributionMockProducer"
