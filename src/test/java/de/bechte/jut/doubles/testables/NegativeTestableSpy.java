/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.testables;

import de.bechte.jut.core.TestResult;

public class NegativeTestableSpy extends TestableSpy {
  public static final String NAME = NegativeTestableSpy.class.getSimpleName();
  public static final String CANONICAL_NAME = NegativeTestableSpy.class.getCanonicalName();

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getUniqueName() {
    return CANONICAL_NAME;
  }

  @Override
  public TestResult createTestResult() {
    return new NegativeTestResultStub();
  }
}