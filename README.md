# 🕷️ Parallel Web Crawler

A high-performance, multi-threaded web crawler built with **Java**, designed to efficiently crawl websites, count word frequencies, and profile execution performance.

---

## 🏆 Project Overview

This project was completed as part of the **Udacity – Advanced Java Programming Techniques** course.  
It modernizes a legacy **sequential web crawler** by transforming it into a **parallel implementation** using advanced Java concurrency patterns.

The result is a scalable, thread-safe crawler with configurable behavior and built-in performance profiling.

---

## ✨ Features

### 🔄 Parallel Processing
- Multi-threaded crawling using **`ForkJoinPool`**
- Recursive task decomposition for efficient URL traversal
- Thread-safe collections:
  - `ConcurrentHashMap`
  - `ConcurrentSkipListSet`

### 📊 Performance Profiling
- Method-level profiling with `@Profiled` annotations
- Dynamic proxy-based interception
- Thread-safe profiling state management
- Human-readable profiling output

### ⚙️ Configuration & Output
- JSON-based configuration (Jackson)
- Configurable:
  - Parallelism level
  - Timeout
  - Crawl depth
  - Ignored URLs and words
- JSON output with sorted word frequencies

### 🧪 Testing & Code Quality
- **38 comprehensive unit tests**
- Integration tests with real crawling scenarios
- Dependency injection using **Guice** for modularity and testability

---

## 🏗️ Architecture

┌─────────────────────────────────────────────┐
│ WebCrawlerMain │
├─────────────────────────────────────────────┤
│ ConfigurationLoader │ CrawlResultWriter │
├─────────────────────────────────────────────┤
│ ParallelWebCrawler (ForkJoinPool) │
├─────────────────────────────────────────────┤
│ PageParserFactory │ Profiler (Proxy) │
└─────────────────────────────────────────────┘

---

## 📁 Project Structure

src/main/java/com/udacity/webcrawler/
├── json/
│   ├── ConfigurationLoader.java
│   ├── CrawlResultWriter.java
│   ├── CrawlerConfiguration.java
│   └── CrawlResult.java
├── profiler/
│   ├── Profiler.java
│   ├── ProfilerImpl.java
│   ├── ProfilingMethodInterceptor.java
│   ├── ProfilingState.java
│   └── Profiled.java
├── ParallelWebCrawler.java
├── SequentialWebCrawler.java
├── WebCrawler.java
├── WordCounts.java
└── main/WebCrawlerMain.java

---

## 🛠️ Technical Details

Parallelism Strategy

Configurable ForkJoinPool

RecursiveAction tasks for URL crawling

Work-stealing for load balancing

Thread Safety

Atomic updates via ConcurrentHashMap.merge()

Lock-free URL tracking with ConcurrentSkipListSet

Safe parallel aggregation of results

Design Patterns Used

Builder Pattern – Configuration & results

Factory Pattern – Page parser creation

Proxy Pattern – Profiling implementation

Dependency Injection – Guice
