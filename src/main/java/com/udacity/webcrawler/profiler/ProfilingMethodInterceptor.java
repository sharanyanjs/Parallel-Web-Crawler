package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * A method interceptor that checks whether {@link Profiled} methods are called and records their
 * execution times.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;
  private final ProfilingState state;
  private final Object delegate;

  ProfilingMethodInterceptor(Clock clock, ProfilingState state, Object delegate) {
    this.clock = Objects.requireNonNull(clock);
    this.state = Objects.requireNonNull(state);
    this.delegate = Objects.requireNonNull(delegate);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // Skip profiling for Object methods (equals, hashCode, toString)
    if (method.getDeclaringClass().equals(Object.class)) {
      return method.invoke(delegate, args);
    }

    // Check if the method has @Profiled annotation
    boolean isProfiled = method.isAnnotationPresent(Profiled.class);

    Instant start = null;
    if (isProfiled) {
      start = clock.instant();
    }

    try {
      // Invoke the original method
      return method.invoke(delegate, args);
    } catch (Throwable t) {
      // Re-throw the original exception
      Throwable cause = t.getCause();
      throw cause != null ? cause : t;
    } finally {
      // Record the duration if the method was profiled
      if (isProfiled && start != null) {
        Instant end = clock.instant();
        Duration duration = Duration.between(start, end);
        // Use delegate.getClass() to get the actual implementation class
        state.record(delegate.getClass(), method, duration);
      }
    }
  }
}