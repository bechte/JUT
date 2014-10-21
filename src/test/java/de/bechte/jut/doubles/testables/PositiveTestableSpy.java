/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.testables;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestStatus;

import java.time.Duration;

public class PositiveTestableSpy extends TestableSpy {
  public static final String NAME = PositiveTestableSpy.class.getSimpleName();
  public static final String CANONICAL_NAME = PositiveTestableSpy.class.getCanonicalName();

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getUniqueName() {
    return CANONICAL_NAME;
  }

  @Override
  protected TestResult createTestResult() {
    return new TestResult(this, TestStatus.SUCCEEDED, Duration.ofMillis(1));
  }
}