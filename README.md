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

flowchart TD
    A[WebCrawlerMain]
    
    A --> B[ConfigurationLoader]
    A --> C[CrawlResultWriter]
    A --> D[ParallelWebCrawler<br/>(ForkJoinPool)]
    
    D --> E[PageParserFactory]
    D --> F[Profiler<br/>(Dynamic Proxy)]
