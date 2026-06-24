# JRSA - Java Runtime Security Agent

> JRSA is an in-process Java security agent that performs runtime monitoring and detection of high-risk behaviors inside JVM applications. Built using the Java Instrumentation API and ByteBuddy, JRSA observes security-sensitive operations during execution and streams runtime telemetry to SMTAP for centralized audit, investigation, and forensic analysis.

---

## Overview

Traditional security mechanisms primarily rely on static analysis, network monitoring, or external host-based agents. These approaches often miss what actually occurs inside the JVM at runtime.

JRSA addresses this gap by operating directly within the JVM and intercepting dangerous operations as they occur.

Current capabilities include:

* Runtime Process Execution Monitoring
* Reflection Usage Detection
* Runtime Policy Evaluation
* Behavioral Risk Scoring
* Centralized Security Event Streaming
* Tamper-Evident Audit Integration via SMTAP

---

## Architecture

```text
                    ┌────────────────────┐
                    │  Protected JVM App │
                    │ (SMTAP/SFB/Any App)│
                    └──────────┬─────────┘
                               │
                      -javaagent:jrsa.jar
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│                          JRSA Agent                        │
├──────────────────────────────────────────────────────────────┤
│ ByteBuddy + Java Instrumentation API                      │
│                                                          │
│ • Process Execution Interceptor                          │
│ • Reflection Interceptor                                │
│ • Runtime Event Generation                              │
│ • Rule Engine                                           │
│ • Risk Analysis Engine                                  │
│ • Policy Evaluation                                     │
└──────────────────────────┬─────────────────────────────────┘
                           │
                           ▼
                ┌──────────────────────┐
                │ SMTAP Event Receiver │
                └──────────┬───────────┘
                           │
                           ▼
                ┌──────────────────────┐
                │ PostgreSQL Audit DB  │
                └──────────────────────┘
```

---

## Features

### Runtime Process Monitoring

Detects execution of external processes initiated from within the JVM.

Examples:

```java
Runtime.getRuntime().exec("cmd.exe");

Runtime.getRuntime().exec("powershell.exe");

new ProcessBuilder("notepad.exe").start();
```

---

### Reflection Monitoring

Observes suspicious reflective operations frequently abused by malware and adversaries.

Example:

```java
Class<?> clazz =
        Class.forName("java.lang.Runtime");

Method method =
        clazz.getMethod("getRuntime");

Object runtime =
        method.invoke(null);
```

---

### Behavioral Analysis

JRSA performs lightweight runtime analysis using:

* Regex-based pattern matching
* Suspicious command detection
* Frequency analysis
* Risk scoring
* Policy-based evaluation

Examples:

| Behavior                  | Risk   |
| ------------------------- | ------ |
| `cmd.exe` execution       | Medium |
| Encoded PowerShell        | High   |
| Multiple process launches | High   |
| Reflection invocation     | Medium |

---

### Centralized Security Telemetry

All detected runtime events are forwarded to SMTAP.

Example event:

```json
{
  "eventType": "PROCESS_EXECUTION",
  "severity": "HIGH",
  "source": "JRSA",
  "details": "powershell.exe -enc SQBtACAAbQBhAGwAaQBjAGkAbwB1AHMA"
}
```

---

## Tech Stack

* Java 17
* Java Instrumentation API
* ByteBuddy
* Maven
* Jackson
* Spring Boot (SMTAP Integration)
* PostgreSQL

---

## Project Structure

```text
src/main/java
│
├── com.ivar.jrsa.agent
│   ├── JRSAAgent.java
│   ├── StressTest.java
│   └── ReflectionDemo.java
│
├── com.ivar.jrsa.interceptor
│   ├── ProcessExecutionInterceptor.java
│   └── ReflectionInterceptor.java
│
├── com.ivar.jrsa.analysis
│   ├── RuleEngine.java
│   ├── RiskLevel.java
│   └── AnalysisResult.java
│
├── com.ivar.jrsa.event
│   ├── SecurityEvent.java
│   ├── SecurityEventType.java
│   └── EventSeverity.java
│
├── com.ivar.jrsa.sender
│   └── SmtapClient.java
│
└── resources
    └── application.properties
```

---

## Building JRSA

Clone repository:

```bash
git clone https://github.com/ravimnm/jrsa.git

cd jrsa
```

Build:

```bash
mvn clean package -DskipTests
```

Generated artifact:

```text
target/jrsa-1.0.jar
```

---

## Running JRSA

Attach JRSA to any JVM application:

```bash
java \
-javaagent:jrsa-1.0.jar \
-jar application.jar
```

Example with SMTAP:

```bash
java \
-javaagent:jrsa-1.0.jar \
-jar audit-compliance-platform.jar
```

---

## Benchmark Results

Benchmarks executed on Windows 10.

| Metric                                   | Result                        |
| ---------------------------------------- | ----------------------------- |
| Event Throughput                         | ~102 events/sec               |
| Average Runtime Event Processing Latency | ~32.6 ms                      |
| Startup Overhead                         | ~9.7%                         |
| Additional Memory Footprint              | ~23.5 MB                      |
| Detection Categories                     | Process Execution, Reflection |

---

## Example Detection Output

```text
[JRSA] Agent started

[JRSA] Process execution detected
Command: powershell.exe -enc SQBtACAAbQBhAGwAaQBjAGkAbwB1AHMA

Risk Score: 90/100

Verdict:
HIGHLY SUSPICIOUS

Recommended Action:
Investigate immediately.
```

---

## Future Work

* File Operation Monitoring
* Dynamic Class Loading Detection
* Network Socket Monitoring
* Native Library Loading Detection
* Runtime Policy Enforcement
* Automatic Threat Response
* MITRE ATT&CK Mapping
* OpenTelemetry Integration
* eBPF Correlation
* Real-time Dashboarding

---

## Performance Summary

JRSA demonstrated lightweight runtime security monitoring capabilities with low overhead while maintaining continuous visibility into high-risk JVM behaviors.

* Sustained throughput of approximately **102 security events per second**
* Average runtime event processing latency of approximately **32 ms**
* Less than **10% application startup overhead**
* Approximately **23 MB additional memory footprint**

---

## Author

**Ravi Sankar Manem**

GitHub: https://github.com/ravimnm

## Integrations

JRSA integrates with the following systems:

| Project | Purpose |
|----------|---------|
| [SMTAP](https://github.com/ravimnm/audit-compliance-platform) | Stores and verifies runtime security telemetry |
| [Secure Finance Backend](https://github.com/ravimnm/secure-finance-backend) | Example protected JVM application monitored by JRSA |
