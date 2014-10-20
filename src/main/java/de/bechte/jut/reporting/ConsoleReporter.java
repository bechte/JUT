/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.reporting;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestResultEntry;
import de.bechte.jut.core.TestResultGroup;

import java.time.Duration;
import java.util.Collection;

public class ConsoleReporter implements Reporter {
  @Override
  public void report(Collection<TestResult> testResults) {
    Long testsRun = testResults.stream().mapToLong(TestResult::getNumberOfTests).sum();
    Long testsSucceeded = testResults.stream().mapToLong(TestResult::getNumberOfSuccessfulTests).sum();
    Long testsFailed = testResults.stream().mapToLong(TestResult::getNumberOfFailingTests).sum();
    printResults(testResults, 0);
    printSummary(testsRun, testsSucceeded, testsFailed);
  }

  private void printSummary(Long testsRun, Long testsSucceeded, Long testsFailed) {
    System.out.println("");
    System.out.println("JUT summary:");
    System.out.println("- Total     : " + testsRun.toString());
    System.out.println("- Succeeded : " + testsSucceeded.toString());
    System.out.println("- Failed    : " + testsFailed.toString());
  }

  private void printResults(Collection<TestResult> testResults, int level) {
    for (TestResult testResult : testResults) {
      printResult(testResult, level);
      if (testResult instanceof TestResultGroup)
        printResults(((TestResultGroup)testResult).getTestResults(), level + 1);
    }
  }

  private void printResult(TestResult testResult, int level) {
    String levelSpacer = getPlaceholderSpaces(0, level * 2);
    String name = testResult.getName();
    boolean isSuccessful = testResult.isSuccessful();
    String duration = formatDuration(testResult.getDuration());

    System.out.println(
        String.format(
            "[%s] %s%s%s%s",
            isSuccessful ? "OKAY" : "FAIL",
            levelSpacer,
            name,
            getPlaceholderSpaces(levelSpacer.length() + name.length() + duration.length(), 120),
            duration
        )
    );

    if (!isSuccessful && testResult instanceof TestResultEntry) {
      ((TestResultEntry)testResult).getFailure().printStackTrace(System.out);
    }
  }

  private String formatDuration(Duration duration) {
    long milliSeconds = duration.toMillis();
    if (milliSeconds < 10000) {
      String value = String.valueOf(milliSeconds);
      return String.format("%s%s ms", getPlaceholderSpaces(value.length(), 10), value);
    } else {
      long seconds = duration.getSeconds();
      milliSeconds -= seconds * 1000;
      String value = String.format("%d.%d", seconds, milliSeconds);
      return String.format("%s%s s ", getPlaceholderSpaces(value.length(), 10), value);
    }
  }


  private String getPlaceholderSpaces(int length, int maxLength) {
    int count = maxLength - length;
    if (count <= 0)
      return String.valueOf("");

    char[] spaces = new char[count];
    for (int i = 0; i < count; i++)
      spaces[i] = ' ';
    return String.valueOf(spaces);
  }
}
