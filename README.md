# 🕷️ Parallel Web Crawler (Java)

A high-performance, multi-threaded web crawler built with Java (17+), demonstrating advanced concurrency patterns, functional programming, and performance profiling.

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

🏗️ Architecture Overview
Layer	Component	Responsibility
Entry Point	WebCrawlerMain	Application entry point; wires dependencies and starts the crawl
Configuration	ConfigurationLoader	Loads and parses JSON configuration
Core Engine	ParallelWebCrawler	Performs parallel crawling using ForkJoinPool
Parsing	PageParserFactory	Creates page parsers for extracting links and words
Profiling	Profiler (Proxy)	Intercepts @Profiled methods and records execution times
Output	CrawlResultWriter	Writes crawl results and statistics to JSON
🔁 Data Flow
Step	Source	Destination	Description
1	WebCrawlerMain	ConfigurationLoader	Load crawler configuration
2	WebCrawlerMain	ParallelWebCrawler	Start crawling process
3	ParallelWebCrawler	PageParserFactory	Parse page content and links
4	ParallelWebCrawler	Profiler	Measure method execution time
5	ParallelWebCrawler	CrawlResultWriter	Persist crawl results

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

🎮 How to Use
👤 Basic Usage Flow
⚙️ Configure – Edit JSON configuration file

🚀 Run Crawler – Execute with config file

📊 View Results – Check output JSON file

📈 Analyze Performance – Review profiling data
