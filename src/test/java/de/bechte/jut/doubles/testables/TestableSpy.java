/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.testables;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.Testable;

public abstract class TestableSpy implements Testable {
  public boolean wasRun;
  public TestResult returnedTestResult;

  @Override
  public TestResult run() {
    wasRun = true;
    returnedTestResult = createTestResult();
    return returnedTestResult;
  }

  protected abstract TestResult createTestResult();
}