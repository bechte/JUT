/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import java.time.Duration;
import java.time.LocalDateTime;

public class TestResultEntry implements TestResult {
  private Testable testable;
  private Throwable failure;

  LocalDateTime testStarted;
  LocalDateTime testEnded;

  public TestResultEntry(Testable testable) {
    this.testable = testable;
    this.testStarted = LocalDateTime.now();
  }

  public void fail(Throwable t) {
    this.testEnded = LocalDateTime.now();
    this.failure = t;
  }

  public void success() {
    this.testEnded = LocalDateTime.now();
  }

  public Throwable getFailure() {
    return failure;
  }

  @Override
  public String getName() {
    return testable.getName();
  }

  @Override
  public String getCanonicalName() {
    return testable.getCanonicalName();
  }

  @Override
  public Duration getDuration() {
    return Duration.between(testStarted, testEnded);
  }

  @Override
  public boolean isSuccessful() {
    return failure == null;
  }

  @Override
  public long getNumberOfTests() {
    return 1;
  }

  @Override
  public long getNumberOfSuccessfulTests() {
    return isSuccessful() ? 1 : 0;
  }

  @Override
  public long getNumberOfFailingTests() {
    return isSuccessful() ? 0 : 1;
  }
}