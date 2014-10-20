/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.reporting;

import de.bechte.jut.core.TestResult;

import java.time.Duration;

public class TestResultEntryStub implements TestResult {
  private final String name;
  private final boolean isSuccessful;
  private final Duration duration;

  public TestResultEntryStub(String name, boolean isSuccessful, Long durationInMillis) {
    this.name = name;
    this.isSuccessful = isSuccessful;
    this.duration = Duration.ofMillis(durationInMillis);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getUniqueName() {
    return TestResultEntryStub.class.getCanonicalName();
  }

  @Override
  public Duration getDuration() {
    return duration;
  }

  @Override
  public boolean isSuccessful() {
    return isSuccessful;
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