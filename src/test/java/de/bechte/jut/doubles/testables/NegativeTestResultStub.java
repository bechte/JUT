/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.testables;

import de.bechte.jut.core.TestResult;

import java.time.Duration;

public class NegativeTestResultStub implements TestResult {
  public static final String NAME = NegativeTestResultStub.class.getSimpleName();
  public static final String CANONICAL_NAME = NegativeTestResultStub.class.getCanonicalName();
  public static final Duration DURATION = Duration.ofMillis(5);

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getUniqueName() {
    return CANONICAL_NAME;
  }

  @Override
  public Duration getDuration() {
    return DURATION;
  }

  @Override
  public boolean isSuccessful() {
    return false;
  }

  @Override
  public long getNumberOfTests() {
    return 1;
  }

  @Override
  public long getNumberOfSuccessfulTests() {
    return 0;
  }

  @Override
  public long getNumberOfFailingTests() {
    return 1;
  }
}