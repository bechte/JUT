/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.reporting;

import de.bechte.jut.core.TestResult;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

public class ConsoleReporter implements Reporter {
  @Override
  public void report(Collection<TestResult> testResults) {
    Long testsRun = Long.valueOf(testResults.size());
    Long testsSucceeded = testResults.stream().filter(r -> r.isSuccessful()).collect(Collectors.counting());
    Long testsFailed = testResults.stream().filter(r -> !r.isSuccessful()).collect(Collectors.counting());
    printHeader(testsRun, testsSucceeded, testsFailed);
    printResults(testResults);
  }

  private void printHeader(Long testsRun, Long testsSucceeded, Long testsFailed) {
    System.out.println("JUT summary:");
    System.out.println("- Total     : " + testsRun.toString());
    System.out.println("- Succeeded : " + testsSucceeded.toString());
    System.out.println("- Failed    : " + testsFailed.toString());
    System.out.println("");
  }

  private void printResults(Collection<TestResult> testResults) {
    for (TestResult testResult : testResults)
      printResult(testResult);
  }

  private void printResult(TestResult testResult) {
    String name = testResult.getTestName();
    boolean isSuccessful = testResult.isSuccessful();
    String duration = formatDuration(testResult.getTestDuration());

    System.out.println(
        String.format(
            "[%s] %s%s%s",
            isSuccessful ? "OKAY" : "FAIL",
            name,
            getPlaceholderSpaces(name.length() + duration.length(), 120),
            duration
        )
    );

    if (!isSuccessful) {
      testResult.getFailure().printStackTrace(System.out);
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
