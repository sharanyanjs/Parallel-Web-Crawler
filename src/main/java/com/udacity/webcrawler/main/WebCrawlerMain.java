package com.udacity.webcrawler.main;

import com.google.inject.Guice;
import com.udacity.webcrawler.WebCrawler;
import com.udacity.webcrawler.WebCrawlerModule;
import com.udacity.webcrawler.json.ConfigurationLoader;
import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.json.CrawlResultWriter;
import com.udacity.webcrawler.json.CrawlerConfiguration;
import com.udacity.webcrawler.profiler.Profiler;
import com.udacity.webcrawler.profiler.ProfilerModule;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class WebCrawlerMain {

  private final CrawlerConfiguration config;

  private WebCrawlerMain(CrawlerConfiguration config) {
    this.config = Objects.requireNonNull(config);
  }

  @Inject
  private WebCrawler crawler;

  @Inject
  private Profiler profiler;

  private void run() throws Exception {
    Guice.createInjector(new WebCrawlerModule(config), new ProfilerModule()).injectMembers(this);

    CrawlResult result = crawler.crawl(config.getStartPages());

    // Write the crawl results to a JSON file (or System.out if no file path is given).
    CrawlResultWriter resultWriter = new CrawlResultWriter(result);

    if (!config.getResultPath().isEmpty()) {
      Path resultPath = Paths.get(config.getResultPath());
      resultWriter.write(resultPath);
      System.out.println("Crawl results written to: " + config.getResultPath());
    } else {
      resultWriter.write(new OutputStreamWriter(System.out));
    }

    // Write the profile data to a text file (or System.out if no file path is given).
    String profileOutputPath = config.getProfileOutputPath();
    try {
      if (!profileOutputPath.isEmpty()) {
        profiler.writeData(Paths.get(profileOutputPath));
      } else {
        profiler.writeData(new OutputStreamWriter(System.out));
      }
    } catch (IOException e) {
      System.err.println("Failed to write profile data: " + e.getMessage());
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Usage: WebCrawlerMain [path]");
      return;
    }

    Path configPath = Paths.get(args[0]);
    CrawlerConfiguration config = new ConfigurationLoader(configPath).load();

    new WebCrawlerMain(config).run();
  }
}