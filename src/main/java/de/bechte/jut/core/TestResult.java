package de.bechte.jut.core;

import java.time.Duration;
import java.time.LocalDateTime;

public class TestResult {
  private Testable testable;
  private Throwable failure;

  private LocalDateTime testStarted;
  private LocalDateTime testEnded;

  public TestResult(Testable testable) {
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

  public String getTestName() {
    return testable.getName();
  }

  public Duration getTestDuration() {
    return Duration.between(testStarted, testEnded);
  }

  public boolean isSuccessful() {
    return failure == null;
  }

  public Throwable getFailure() {
    return failure;
  }
}