/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import de.bechte.jut.reporting.ConsoleReporter;
import de.bechte.jut.testables.RuntimeTestableFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import static de.bechte.jut.core.ApplicationContext.testableFactory;

public class Bootstrap {
  private String[] arguments;
  private Optional<Reporter> reporter;

  public static void main(String... arguments) {
    testableFactory = new RuntimeTestableFactory();

    Bootstrap bootstrap = new Bootstrap(arguments);
    bootstrap.setReporter(new ConsoleReporter(System.out));
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
      testResults.add(testableFactory.forName(argument).run());
    reporter.ifPresent(r -> r.report(testResults));
  }
}