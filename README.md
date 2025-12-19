# 🕷️ Parallel Web Crawler (Java)

A high-performance, multi-threaded web crawler built with Java (17+), demonstrating advanced concurrency patterns, functional programming, and performance profiling.

## 📋 Table of Contents
- [✨ Features](#-features)
- [🏗️ Architecture](#️-architecture)
- [🚀 Quick Start](#-quick-start)
- [📁 Project Structure](#-project-structure)
- [🎮 How to Use](#-how-to-use)
- [🔧 Code Examples](#-code-examples)
- [🧪 Testing](#-testing)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

## ✨ Features

### 🎯 Core Functionality
- **✅ Parallel Crawling** – Multi-threaded web page processing with `ForkJoinPool`
- **✅ Performance Profiling** – Method-level execution time tracking with `@Profiled` annotations
- **✅ JSON Configuration** – Flexible configuration via JSON files
- **✅ Word Frequency Analysis** – Count and rank most frequent words
- **✅ URL Deduplication** – Thread-safe visited URL tracking

### ⚡ Technical Highlights
- **🏗️ Concurrency Patterns** – `RecursiveAction`, `ConcurrentHashMap`, `ConcurrentSkipListSet`
- **🔄 Functional Programming** – Stream API for word counting without loops
- **🏭 Design Patterns** – Builder, Factory, Proxy, Dependency Injection
- **📊 Dynamic Proxies** – Non-invasive method interception for profiling
- **🎯 Dependency Injection** – Guice for loose coupling and testability
- **📦 JSON Processing** – Jackson library for configuration and output

### 🌟 Unique Enhancements
- **⚡ Smart Parallelism** – Auto-scales based on available CPU cores
- **⏱️ Deadline Management** – Respects timeout constraints gracefully
- **📈 Depth Limiting** – Prevents infinite crawling with configurable depth
- **🔗 Link Extraction** – Recursive discovery of nested pages
- **📝 Profiling Output** – Detailed method execution timing reports
- **🛡️ Error Resilience** – Continues crawling despite individual page failures

## 🏗️ Architecture

```mermaid
flowchart TD
    A[🚀 WebCrawlerMain<br/>Main Entry Point] --> B[📄 JSON Config File]
    B --> C[⚙️ ConfigurationLoader]
    C --> D{🔄 Which Implementation?}
    
    D -->|Sequential| E[🦥 SequentialWebCrawler]
    D -->|Parallel| F[⚡ ParallelWebCrawler]
    
    F --> G[🏊‍♂️ ForkJoinPool]
    G --> H[🎯 CrawlTask<br/>RecursiveAction]
    
    H --> I{📄 Parse HTML Page}
    I -->|Extract Words| J[📊 ConcurrentHashMap<br/>Word Counts]
    I -->|Extract Links| K[🔗 ConcurrentSkipListSet<br/>Visited URLs]
    
    J --> L[📈 WordCounts.sort<br/>Functional Stream API]
    L --> M[💾 CrawlResultWriter]
    
    H --> N{⏱️ Timeout?}
    N -->|No| O{📏 Max Depth?}
    N -->|Yes| P[⏹️ Stop Processing]
    O -->|No| Q{🔗 Already Visited?}
    O -->|Yes| P
    Q -->|No| H
    Q -->|Yes| P
    
    R[🎭 Profiler Module] --> S[🔍 ProfilingMethodInterceptor]
    S --> T[📊 ProfilingState]
    T --> U[📝 Profile Output]
    
    E --> M
    M --> V[🎉 JSON Results]
    
    style A fill:#ff6b6b,stroke:#333,stroke-width:2px
    style F fill:#4ecdc4,stroke:#333,stroke-width:2px
    style G fill:#45b7d1,stroke:#333,stroke-width:2px
    style J fill:#96ceb4,stroke:#333,stroke-width:2px
    style K fill:#feca57,stroke:#333,stroke-width:2px
    style R fill:#ff9ff3,stroke:#333,stroke-width:2px
    style V fill:#1dd1a1,stroke:#333,stroke-width:2px
Component Architecture Diagram
graph TB
    subgraph "📦 Data Layer"
        D1[ConcurrentHashMap<br/>Word Counts]
        D2[ConcurrentSkipListSet<br/>Visited URLs]
        D3[ProfilingState<br/>Method Timings]
    end
    
    subgraph "🔧 Service Layer"
        S1[ParallelWebCrawler]
        S2[PageParserFactory]
        S3[WordCounts Utility]
    end
    
    subgraph "🎭 Profiler Layer"
        P1[Profiler Interface]
        P2[ProfilerImpl]
        P3[ProfilingMethodInterceptor<br/>Dynamic Proxy]
    end
    
    subgraph "📄 IO Layer"
        I1[ConfigurationLoader<br/>JSON → Config]
        I2[CrawlResultWriter<br/>Results → JSON]
        I3[WebCrawlerMain<br/>CLI Interface]
    end
    
    subgraph "🏗️ Framework Layer"
        F1[ForkJoinPool<br/>Thread Management]
        F2[RecursiveAction<br/>Task Decomposition]
        F3[Guice DI<br/>Dependency Injection]
    end
    
    I3 --> I1
    I1 --> S1
    S1 --> F1
    F1 --> F2
    F2 --> S2
    S2 --> D1
    S2 --> D2
    S1 --> S3
    S3 --> I2
    
    S1 -.-> P2
    P2 --> P3
    P3 --> D3
    D3 --> I2
    
    style D1 fill:#96ceb4
    style D2 fill:#feca57
    style D3 fill:#ff9ff3
    style S1 fill:#4ecdc4
    style P2 fill:#ff6b6b
    style F1 fill:#45b7d1
    style I1 fill:#1dd1a1
Data Flow Architecture
sequenceDiagram
    participant User as 👤 User
    participant Main as 🚀 WebCrawlerMain
    participant Config as ⚙️ ConfigLoader
    participant Crawler as ⚡ ParallelWebCrawler
    participant Pool as 🏊‍♂️ ForkJoinPool
    participant Task as 🎯 CrawlTask
    participant Parser as 🔍 PageParser
    participant Words as 📊 WordCounter
    participant URLs as 🔗 URLTracker
    participant Profiler as 📈 Profiler
    participant Output as 💾 ResultWriter

    User->>Main: Execute with config.json
    Main->>Config: Load configuration
    Config-->>Main: Return config object
    
    Main->>Crawler: Start crawl(startingURLs)
    Crawler->>Pool: Create ForkJoinPool
    Crawler->>Words: Initialize ConcurrentHashMap
    Crawler->>URLs: Initialize ConcurrentSkipListSet
    
    loop For each starting URL
        Crawler->>Pool: Submit CrawlTask
        Pool->>Task: Execute compute()
        
        Task->>Task: Check depth & timeout
        Task->>URLs: Try add URL (atomic)
        URLs-->>Task: Success/Failure
        
        alt URL not visited
            Task->>Profiler: Start timing (if @Profiled)
            Task->>Parser: Parse HTML page
            Parser-->>Task: Return words & links
            Task->>Words: Merge word counts (atomic)
            
            loop For each discovered link
                Task->>Pool: Submit new CrawlTask
            end
            
            Task->>Profiler: Record duration
        end
    end
    
    Pool-->>Crawler: All tasks complete
    Crawler->>Words: Get sorted word counts
    Words-->>Crawler: Return top N words
    Crawler->>Output: Write results
    Output-->>User: Save to crawlResults.json
    
    Profiler->>Output: Write profiling data
    Output-->>User: Save to profileData.txt
Thread Concurrency Model
graph TD
    subgraph "🏊‍♂️ ForkJoinPool (4 Threads)"
        T1[Thread 1]
        T2[Thread 2]
        T3[Thread 3]
        T4[Thread 4]
    end
    
    subgraph "📋 Task Queue"
        Q1[Task A: example.com]
        Q2[Task B: example.org]
        Q3[Task C: example.net]
        Q4[...]
    end
    
    subgraph "🔗 Shared State"
        SH1[ConcurrentHashMap<br/>word → count]
        SH2[ConcurrentSkipListSet<br/>visited URLs]
    end
    
    T1 --> Q1
    T2 --> Q2
    T3 --> Q3
    T4 --> Q4
    
    Q1 -->|Process| SA1[Subtask A1]
    Q1 -->|Process| SA2[Subtask A2]
    
    Q2 -->|Process| SB1[Subtask B1]
    Q2 -->|Process| SB2[Subtask B2]
    
    SA1 --> SH1
    SA2 --> SH1
    SB1 --> SH1
    SB2 --> SH1
    
    SA1 --> SH2
    SA2 --> SH2
    SB1 --> SH2
    SB2 --> SH2
    
    style T1 fill:#ff6b6b
    style T2 fill:#4ecdc4
    style T3 fill:#feca57
    style T4 fill:#96ceb4
    style SH1 fill:#45b7d1
    style SH2 fill:#ff9ff3
