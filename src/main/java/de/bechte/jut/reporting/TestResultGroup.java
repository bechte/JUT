/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.reporting;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.Testable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TestResultGroup implements TestResult {
  private Testable testable;
  private Collection<TestResult> testResults;

  public TestResultGroup(Testable testable) {
    this.testable = testable;
    this.testResults = new LinkedList<>();
  }

  public void addTestResults(Collection<? extends TestResult> testResults) {
    this.testResults.addAll(testResults);
  }

  public Collection<TestResult> getTestResults() {
    return testResults;
  }

  @Override
  public String getName() {
    return testable.getName();
  }

  @Override
  public String getUniqueName() {
    return testable.getUniqueName();
  }

  @Override
  public Duration getDuration() {
    return Duration.of(getTestResults().stream()
        .map(t -> t.getDuration())
        .collect(Collectors.summingLong(Duration::toMillis)), ChronoUnit.MILLIS);
  }

  @Override
  public boolean isSuccessful() {
    return getTestResults().stream()
        .filter(t -> !t.isSuccessful())
        .count() == 0;
  }

  @Override
  public long getNumberOfTests() {
    return getTestResults().stream()
        .collect(Collectors.summarizingLong(TestResult::getNumberOfTests))
        .getSum();
  }

  @Override
  public long getNumberOfSuccessfulTests() {
    return getTestResults().stream()
        .collect(Collectors.summarizingLong(TestResult::getNumberOfSuccessfulTests))
        .getSum();
  }

  @Override
  public long getNumberOfFailingTests() {
    return getTestResults().stream()
        .collect(Collectors.summarizingLong(TestResult::getNumberOfFailingTests))
        .getSum();
  }
}