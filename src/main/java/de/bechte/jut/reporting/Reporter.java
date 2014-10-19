package de.bechte.jut.reporting;

import de.bechte.jut.core.TestResult;

import java.util.Collection;

public interface Reporter {
  void report(Collection<TestResult> testResults);
}