/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import de.bechte.jut.reporting.ConsoleReporter;
import de.bechte.jut.reporting.Reporter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class Bootstrap {
  private String[] arguments;
  private Optional<Reporter> reporter;

  public static void main(String... arguments) {
    Bootstrap bootstrap = new Bootstrap(arguments);
    bootstrap.setReporter(new ConsoleReporter());
    bootstrap.run();
  }

  public Bootstrap(String... arguments) {
    this.arguments = arguments;
    this.reporter = Optional.empty();
  }

  public void setReporter(Reporter reporter) {
    this.reporter = Optional.ofNullable(reporter);
  }

  public void run() {
    Collection<TestResult> testResults = new LinkedList<>();
    for (String argument : arguments)
      testResults.add(Testable.forName(argument).runTest());
    reporter.ifPresent(r -> r.report(testResults));
  }
}