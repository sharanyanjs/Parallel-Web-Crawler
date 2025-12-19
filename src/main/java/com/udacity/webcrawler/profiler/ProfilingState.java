package com.udacity.webcrawler.profiler;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A container class that holds profiling information about the execution of {@link Profiled}
 * methods.
 *
 * <p>This class is thread-safe.
 */
public final class ProfilingState {

  private final Map<String, List<Duration>> state = new HashMap<>();

  /**
   * Records that the given {@link Profiled} method executed for the specified {@link Duration}.
   *
   * @param klass the class containing the profiled method.
   * @param method the profiled method.
   * @param duration the length of time the method took to execute.
   */
  public void record(Class<?> klass, java.lang.reflect.Method method, Duration duration) {
    Objects.requireNonNull(klass);
    Objects.requireNonNull(method);
    Objects.requireNonNull(duration);

    synchronized (state) {
      String key = klass.getName() + "#" + method.getName();
      state.computeIfAbsent(key, k -> new ArrayList<>()).add(duration);
    }
  }

  /**
   * Returns a {@link Map} containing all the profiling data. The keys of the map are fully
   * qualified method names, formatted as "{@code com.example.ClassName#methodName}". The values
   * are {@link List}s of {@link Duration}s, each of which is a recorded invocation of that method.
   *
   * <p>The returned map is an unmodifiable copy of the profiling data.
   */
  public Map<String, List<Duration>> getData() {
    synchronized (state) {
      Map<String, List<Duration>> copy = new HashMap<>();
      for (Map.Entry<String, List<Duration>> entry : state.entrySet()) {
        copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
      }
      return Collections.unmodifiableMap(copy);
    }
  }

  /**
   * Returns the number of times the given {@link Profiled} method was called.
   *
   * @param klass the class containing the method.
   * @param method the profiled method.
   * @return the call count.
   */
  public int getInvocationCount(Class<?> klass, java.lang.reflect.Method method) {
    Objects.requireNonNull(klass);
    Objects.requireNonNull(method);

    String key = klass.getName() + "#" + method.getName();
    synchronized (state) {
      List<Duration> durations = state.get(key);
      return durations != null ? durations.size() : 0;
    }
  }

  /**
   * Returns the total duration of all recorded invocations of the given {@link Profiled} method.
   *
   * @param klass the class containing the method.
   * @param method the profiled method.
   * @return the total duration.
   */
  public Duration getTotalDuration(Class<?> klass, java.lang.reflect.Method method) {
    Objects.requireNonNull(klass);
    Objects.requireNonNull(method);

    String key = klass.getName() + "#" + method.getName();
    synchronized (state) {
      List<Duration> durations = state.get(key);
      if (durations == null) {
        return Duration.ZERO;
      }
      return durations.stream().reduce(Duration.ZERO, Duration::plus);
    }
  }

  /**
   * Formats this {@link ProfilingState} as a string that can be written to a file or printed to the
   * console.
   *
   * <p>The format of each line is:
   * <pre>
   *   fully.qualified.ClassName#methodName took 1m 30s 500ms
   * </pre>
   *
   * <p>If the same method was called multiple times, each invocation appears on its own line.
   *
   * @return the formatted string.
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    synchronized (state) {
      List<String> keys = new ArrayList<>(state.keySet());
      Collections.sort(keys);

      for (String key : keys) {
        List<Duration> durations = state.get(key);
        for (Duration duration : durations) {
          long minutes = duration.toMinutes();
          long seconds = duration.minusMinutes(minutes).getSeconds();
          long milliseconds = duration.minusMinutes(minutes).minusSeconds(seconds).toMillis();

          builder.append(key)
                  .append(" took ")
                  .append(minutes)
                  .append('m')
                  .append(' ')
                  .append(seconds)
                  .append('s')
                  .append(' ')
                  .append(milliseconds)
                  .append("ms")
                  .append(System.lineSeparator());
        }
      }
    }
    return builder.toString().trim();
  }
}