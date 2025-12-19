package com.udacity.webcrawler.profiler;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

/**
 * A method interceptor that checks whether {@link Profiled} methods are called and records their
 * execution times.
 */
final class ProfilerImpl implements Profiler {

  private final Clock clock;
  private final ProfilingState state = new ProfilingState();

  @Inject
  ProfilerImpl(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
  }

  @Override
  public <T> T wrap(Class<T> klass, T delegate) {
    Objects.requireNonNull(klass);
    Objects.requireNonNull(delegate);

    // Check if the class has any @Profiled methods
    boolean hasProfiledMethods = Arrays.stream(klass.getDeclaredMethods())
            .anyMatch(method -> method.isAnnotationPresent(Profiled.class));

    if (!hasProfiledMethods) {
      throw new IllegalArgumentException(klass.getName() + " does not have any @Profiled methods");
    }

    // Create a dynamic proxy
    Object proxy = Proxy.newProxyInstance(
            klass.getClassLoader(),
            new Class[]{klass},
            new ProfilingMethodInterceptor(clock, state, delegate)  // Back to 3 parameters
    );

    return klass.cast(proxy);
  }

  @Override
  public void writeData(Path path) throws IOException {
    Objects.requireNonNull(path);

    try (BufferedWriter writer = Files.newBufferedWriter(path,
            StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
      writeData(writer);
    }
  }

  @Override
  public void writeData(Writer writer) throws IOException {
    Objects.requireNonNull(writer);

    writer.write("Run at " + Instant.now(clock) + "\n");
    writer.write(state.toString());
    writer.write("\n");
    writer.flush();
  }
}