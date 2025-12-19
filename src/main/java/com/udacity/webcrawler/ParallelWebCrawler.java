package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A concrete implementation of {@link WebCrawler} that runs multiple threads on a
 * {@link ForkJoinPool} to efficiently crawl the web.
 */
public final class ParallelWebCrawler implements WebCrawler {
  private final Clock clock;
  private final PageParserFactory parserFactory;
  private final Duration timeout;
  private final int popularWordCount;
  private final int maxDepth;
  private final List<Pattern> ignoredUrls;
  private final ForkJoinPool pool;

  @Inject
  ParallelWebCrawler(
          Clock clock,
          PageParserFactory parserFactory,
          @Timeout Duration timeout,
          @PopularWordCount int popularWordCount,
          @MaxDepth int maxDepth,
          @IgnoredUrls List<Pattern> ignoredUrls,
          @TargetParallelism int parallelism) {
    this.clock = clock;
    this.parserFactory = parserFactory;
    this.timeout = timeout;
    this.popularWordCount = popularWordCount;
    this.maxDepth = maxDepth;
    this.ignoredUrls = ignoredUrls;

    // Create the ForkJoinPool with the desired parallelism
    this.pool = new ForkJoinPool(Math.min(parallelism, getMaxParallelism()));
  }

  @Override
  public CrawlResult crawl(List<String> startingUrls) {
    Instant deadline = clock.instant().plus(timeout);

    // Thread-safe collections for shared data
    ConcurrentMap<String, Integer> counts = new ConcurrentHashMap<>();
    Set<String> visitedUrls = new ConcurrentSkipListSet<>();

    // Create tasks for each starting URL
    List<CrawlTask> tasks = startingUrls.stream()
            .map(url -> new CrawlTask(url, deadline, 0, counts, visitedUrls))
            .collect(Collectors.toList());

    // Invoke all tasks using ForkJoinTask.invokeAll() for RecursiveAction
    ForkJoinTask.invokeAll(tasks);

    pool.shutdown();

    // Sort and limit the word counts
    Map<String, Integer> sortedCounts = WordCounts.sort(counts, popularWordCount);

    // Return the crawl result
    return new CrawlResult.Builder()
            .setWordCounts(sortedCounts)
            .setUrlsVisited(visitedUrls.size())
            .build();
  }

  @Override
  public int getMaxParallelism() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * A RecursiveAction that crawls a single URL and recursively crawls its links.
   */
  private final class CrawlTask extends RecursiveAction {
    private final String url;
    private final Instant deadline;
    private final int depth;
    private final ConcurrentMap<String, Integer> counts;
    private final Set<String> visitedUrls;

    CrawlTask(
            String url,
            Instant deadline,
            int depth,
            ConcurrentMap<String, Integer> counts,
            Set<String> visitedUrls) {
      this.url = url;
      this.deadline = deadline;
      this.depth = depth;
      this.counts = counts;
      this.visitedUrls = visitedUrls;
    }

    @Override
    protected void compute() {
      // Check if we should stop crawling
      if (depth >= maxDepth) {
        return;
      }

      // Check if we've run out of time
      if (clock.instant().isAfter(deadline)) {
        return;
      }

      // Check if URL matches any ignored pattern
      for (Pattern pattern : ignoredUrls) {
        if (pattern.matcher(url).matches()) {
          return;
        }
      }

      // Try to add the URL to visited set (atomic operation)
      if (!visitedUrls.add(url)) {
        return; // Already visited
      }

      try {
        // Parse the page
        PageParser.Result result = parserFactory.get(url).parse();

        // Process words from this page
        for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
          counts.merge(e.getKey(), e.getValue(), Integer::sum);
        }

        // Create subtasks for links
        List<CrawlTask> subtasks = new ArrayList<>();
        for (String link : result.getLinks()) {
          CrawlTask subtask = new CrawlTask(
                  link, deadline, depth + 1, counts, visitedUrls);
          subtasks.add(subtask);
        }

        // Invoke all subtasks
        ForkJoinTask.invokeAll(subtasks);

      } catch (Exception e) {
        // Log but continue - don't stop the entire crawl for one error
        System.err.println("Error crawling " + url + ": " + e.getMessage());
      }
    }
  }
}