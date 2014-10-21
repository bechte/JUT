/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TestResult {
  private Testable testable;
  private TestStatus status;
  private Duration duration;

  private Collection<TestResult> testResults;
  private Collection<Throwable> failures;

  public TestResult(Testable testable, TestStatus status, Duration duration) {
    this.testable = testable;
    this.status = status;
    this.duration = duration;

    this.testResults = new LinkedList<>();
    this.failures = new LinkedList<>();
  }

  public String getName() {
    return testable.getName();
  }

  public String getUniqueName() {
    return testable.getUniqueName();
  }

  public TestStatus getStatus() {
    return testResults.isEmpty() ? status :
        getNumberOfFailingTests() > 0 ? TestStatus.FAILED :
        getNumberOfSkippedTests() > 0 ? TestStatus.SKIPPED :
        TestStatus.SUCCEEDED;
  }

  public Duration getDuration() {
    return testResults.isEmpty() ? duration :
        Duration.ofMillis(testResults.stream()
            .map(r -> r.getDuration())
            .collect(Collectors.summarizingLong(Duration::toMillis))
            .getSum());
  }

  public void addTestResult(TestResult testResult) {
    this.testResults.add(testResult);
  }

  public void addTestResults(Collection<TestResult> testResults) {
    this.testResults.addAll(testResults);
  }

  public Collection<TestResult> getTestResults() {
    return Collections.unmodifiableCollection(testResults);
  }

  public void addFailure(Throwable failure) {
    this.failures.add(failure);
  }

  public void addFailures(Collection<Throwable> failures) {
    this.failures.addAll(failures);
  }

  public Collection<Throwable> getFailures() {
    return Collections.unmodifiableCollection(failures);
  }

  public long getNumberOfTests() {
    return testResults.isEmpty() ? 1 :
        testResults.stream()
            .collect(Collectors.summarizingLong(TestResult::getNumberOfTests))
            .getSum();
  }

  public long getNumberOfSuccessfulTests() {
    return testResults.isEmpty() ? ((status == TestStatus.SUCCEEDED) ? 1 : 0) :
        testResults.stream()
            .collect(Collectors.summarizingLong(TestResult::getNumberOfSuccessfulTests))
            .getSum();
  }

  public long getNumberOfFailingTests() {
    return testResults.isEmpty() ? ((status == TestStatus.FAILED) ? 1 : 0) :
        testResults.stream()
            .collect(Collectors.summarizingLong(TestResult::getNumberOfFailingTests))
            .getSum();
  }

  public long getNumberOfSkippedTests() {
    return testResults.isEmpty() ? ((status == TestStatus.SKIPPED) ? 1 : 0) :
        testResults.stream()
            .collect(Collectors.summarizingLong(TestResult::getNumberOfSkippedTests))
            .getSum();
  }
}