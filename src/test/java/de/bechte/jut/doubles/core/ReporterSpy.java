/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.core;

import de.bechte.jut.core.Reporter;
import de.bechte.jut.core.TestResult;

import java.util.Collection;

public class ReporterSpy implements Reporter {
  public Collection<TestResult> reportedTestResults;
  public boolean wasReported;

  @Override
  public void report(Collection<TestResult> testResults) {
    wasReported = true;
    reportedTestResults = testResults;
  }
}