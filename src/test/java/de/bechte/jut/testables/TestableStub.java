/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.Testable;

public class TestableStub implements Testable {
  public static final String NAME = "TESTABLE NAME";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public TestResult runTest() {
    return null;
  }
}