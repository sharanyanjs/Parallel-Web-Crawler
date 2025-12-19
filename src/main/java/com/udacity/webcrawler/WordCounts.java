package com.udacity.webcrawler;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class that sorts the word count map.
 */
public final class WordCounts {

  /**
   * Given an unsorted map of word counts, returns a new map whose word counts are sorted according
   * to the provided {@link WordCountComparator}, and includes only the top {@param maxWordCount}
   * words and counts.
   *
   * @param wordCounts       the unsorted map of word counts.
   * @param maxWordCount the maximum number of entries to include in the returned map. If {@code
   *                     maxWordCount} is less than 1, returns an empty map.
   * @return a map containing the top {@param maxWordCount} words and counts, sorted by frequency
   * and then by word.
   */
  public static Map<String, Integer> sort(Map<String, Integer> wordCounts, int maxWordCount) {
    // If maxWordCount is less than 1, return empty map
    if (maxWordCount < 1) {
      return Collections.emptyMap();
    }

    return wordCounts.entrySet()
            .stream()
            .sorted(new WordCountComparator())
            .limit(maxWordCount)
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
            ));
  }

  /**
   * A {@link Comparator} that sorts word count pairs correctly:
   *
   * <p>
   * <ol>
   *   <li>First sorting by word count, ranking more frequent words higher.</li>
   *   <li>If words have the same frequency, then longer words are ranked higher.</li>
   *   <li>If words have the same frequency and length, then alphabetically by word.</li>
   * </ol>
   */
  private static final class WordCountComparator implements Comparator<Map.Entry<String, Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
      if (!a.getValue().equals(b.getValue())) {
        return b.getValue() - a.getValue();
      }
      if (a.getKey().length() != b.getKey().length()) {
        return b.getKey().length() - a.getKey().length();
      }
      return a.getKey().compareTo(b.getKey());
    }
  }
}