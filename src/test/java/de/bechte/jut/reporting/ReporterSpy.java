/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.reporting;

import de.bechte.jut.core.TestResult;

import java.util.Collection;

public class ReporterSpy implements Reporter {
  public Collection<TestResult> testResults;

  @Override
  public void report(Collection<TestResult> testResults) {
    this.testResults = testResults;
  }
}