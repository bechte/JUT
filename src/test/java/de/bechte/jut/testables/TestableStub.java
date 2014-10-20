/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.Testable;

public class TestableStub implements Testable {
  public static final String NAME = TestableStub.class.getSimpleName();
  public static final String CANONICAL_NAME = TestableStub.class.getCanonicalName();

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getCanonicalName() {
    return CANONICAL_NAME;
  }

  @Override
  public TestResult runTest() {
    return null;
  }
}