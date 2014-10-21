/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.reporting;

import de.bechte.jut.core.Reporter;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestStatus;

import java.io.PrintStream;
import java.time.Duration;
import java.util.Collection;

public class ConsoleReporter implements Reporter {
  private PrintStream out;

  public ConsoleReporter(PrintStream out) {
    this.out = out;
  }

  @Override
  public void report(Collection<TestResult> testResults) {
    Long testsRun = testResults.stream().mapToLong(TestResult::getNumberOfTests).sum();
    Long testsSucceeded = testResults.stream().mapToLong(TestResult::getNumberOfSuccessfulTests).sum();
    Long testsSkipped = testResults.stream().mapToLong(TestResult::getNumberOfSkippedTests).sum();
    Long testsFailed = testResults.stream().mapToLong(TestResult::getNumberOfFailingTests).sum();
    printResults(testResults, 0);
    printSummary(testsRun, testsSucceeded, testsSkipped, testsFailed);
  }

  private void printSummary(Long testsRun, Long testsSucceeded, Long testSkipped, Long testsFailed) {
    out.println("");
    out.println("JUT summary:");
    out.println("- Total     : " + testsRun.toString());
    out.println("- Succeeded : " + testsSucceeded.toString());
    out.println("- Skipped   : " + testSkipped.toString());
    out.println("- Failed    : " + testsFailed.toString());
  }

  private void printResults(Collection<TestResult> testResults, int level) {
    for (TestResult testResult : testResults) {
      printResult(testResult, level);
      printResults(testResult.getTestResults(), level + 1);
    }
  }

  private void printResult(TestResult testResult, int level) {
    String statusSpacer = getPlaceholderSpaces(testResult.getStatus().toString().length(), 10);
    String levelSpacer = getPlaceholderSpaces(0, level * 2);
    String name = testResult.getName();
    String duration = formatDuration(testResult.getDuration());

    out.println(
        String.format(
            "[%s%s] %s%s%s%s",
            statusSpacer,
            testResult.getStatus(),
            levelSpacer,
            name,
            getPlaceholderSpaces(levelSpacer.length() + name.length() + duration.length(), 120),
            duration
        )
    );

    if (testResult.getStatus() == TestStatus.FAILED && testResult.getTestResults().isEmpty())
      testResult.getFailures().forEach(f -> f.printStackTrace(out));
  }

  private String formatDuration(Duration duration) {
    String value = String.valueOf(duration.toMillis());
    return String.format("%s%s ms", getPlaceholderSpaces(value.length(), 10), value);
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
