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

+--------------------+
|  WebCrawlerMain    |
+--------------------+
          |
          v
+--------------------+
| ConfigurationLoader|
+--------------------+
          |
          v
+--------------------+
| ParallelWebCrawler |
|  (ForkJoinPool)    |
+--------------------+
          |
          v
+--------------------+
| PageParserFactory  |
+--------------------+
          |
          v
+--------------------+
| Profiler (Proxy)   |
+--------------------+
          |
          v
+--------------------+
| CrawlResultWriter  |
+--------------------+


## 🚀 Quick Start

### Prerequisites
- ☕ **Java JDK 17+**
- 🏗️ **Maven 3.6.3+**
- 🌐 **Internet connection** (for web crawling)
- 💻 **IntelliJ IDEA** (recommended) or any Java IDE

### Installation
```bash
# 1. Clone the repository
git clone https://github.com/yourusername/parallel-web-crawler.git

# 2. Navigate to the project directory
cd parallel-web-crawler

# 3. Build the project
mvn clean package

# Run the sequential crawler (legacy implementation)
java -jar target/udacity-webcrawler-1.0.jar src/main/config/sample_config_sequential.json

# Run the parallel crawler (4 threads)
java -jar target/udacity-webcrawler-1.0.jar src/main/config/sample_config.json

### 🎮 How to Use
👤 Basic Usage Flow
⚙️ Configure – Edit JSON configuration file

🚀 Run Crawler – Execute with config file

📊 View Results – Check output JSON file

📈 Analyze Performance – Review profiling data
