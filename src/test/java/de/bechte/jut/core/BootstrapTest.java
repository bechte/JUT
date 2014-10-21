/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import de.bechte.jut.annotations.*;
import de.bechte.jut.doubles.core.PositiveTestableFactorySpy;
import de.bechte.jut.doubles.core.ReporterSpy;
import de.bechte.jut.doubles.core.TestableFactorySpy;

import java.util.stream.Collectors;

import static de.bechte.jut.core.ApplicationContext.testableFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BootstrapTest {
  private static String[] ARGUMENTS = new String[] { "First", "Second", "Third" };

  private TestableFactory oldFactory;
  private TestableFactorySpy testableFactorySpy = new PositiveTestableFactorySpy();
  private ReporterSpy reporter = new ReporterSpy();
  private Bootstrap bootstrap = new Bootstrap(ARGUMENTS);

  @Before
  public void saveTestableFactorySpyFromApplicationContext() throws Exception {
    oldFactory = testableFactory;
    testableFactory = testableFactorySpy;
  }

  @After
  public void restoreSavedTestableFactoryInApplicationContext() throws Exception {
    testableFactory = oldFactory;
    oldFactory = null;
  }

  @Test
  public void callsTestableFactoryForEachArgument() throws Exception {
    bootstrap.run();
    assertThat(testableFactorySpy.invocationsForName, containsInAnyOrder(ARGUMENTS));
  }

  @Test
  public void callsRunOnEachTestableStub() throws Exception {
    bootstrap.run();
    testableFactorySpy.allTestables.forEach(t -> assertThat(t.wasRun, is(true)));
  }

  @Test
  public void callsTheReporterWithAllTestResults() throws Exception {
    bootstrap.setReporter(reporter);
    bootstrap.run();

    assertThat(reporter.wasReported, is(true));
    assertThat(reporter.reportedTestResults.size(), is(3));
    assertThat(reporter.reportedTestResults, containsInAnyOrder(
        testableFactorySpy.allTestables.stream()
            .map(t -> t.returnedTestResult)
            .collect(Collectors.toList())
            .toArray()
    ));
  }
}