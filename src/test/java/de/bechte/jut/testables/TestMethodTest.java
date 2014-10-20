/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.doubles.samples.TestClassWithSingleTestForTiming;
import de.bechte.jut.doubles.testables.TestClassMock;
import de.bechte.jut.doubles.testables.ThrowingTestMethodMock;
import de.bechte.jut.reporting.TestResultEntry;

import java.lang.reflect.InvocationTargetException;
import java.time.chrono.ChronoLocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class TestMethodTest {
  private TestClassMock<?> testClass;
  private TestMethod<?> testMethod;

  @Context
  public class GivenValidTestMethod {
    public static final String TEST_METHOD_NAME = "toString";

    @Before
    public void createTestMethod() throws Exception {
      testClass = new TestClassMock(new Object());
      testMethod = new TestMethod<>(testClass, Object.class.getMethod(TEST_METHOD_NAME));
    }

    @Test
    public void getNameReturnsNameOfMethod() throws Exception {
      assertThat(testMethod.getName(), is(TEST_METHOD_NAME));
    }

    @Test
    public void getUniqueNameReturnsFullyQualifiedNameOfMethod() throws Exception {
      assertThat(testMethod.getUniqueName(), is(String.format(
          TestMethod.UNIQUE_NAME_FORMAT, testClass.getUniqueName(), TEST_METHOD_NAME)));
    }

    @Test
    public void testResultContainsNoFailure() throws Exception {
      TestResult testResult = testMethod.run();
      assertThat(testResult.isSuccessful(), is(true));
      assertThat(((TestResultEntry)testResult).getFailure(), is(nullValue()));
    }

    @Test
    public void runTestMethodInvokesMethodsOfTheTestClass() throws Throwable {
      testMethod.run();
      assertThat(testClass.testInstanceOnSetup, is(testClass.instance));
      assertThat(testClass.testInstanceOnTeardown, is(testClass.instance));
    }
  }

  @Context
  public class GivenTestClassWithSingleTestForTiming {
    @Before
    public void createTestMethod() throws Exception {
      testClass = new TestClassMock(new TestClassWithSingleTestForTiming());
      testMethod = new TestMethod<>(testClass, TestClassWithSingleTestForTiming.class.getMethod("testMethod"));
    }

    @Test
    public void duringRun_setupGetsCalledBeforeTestMethod() throws Exception {
      testMethod.run();

      TestClassWithSingleTestForTiming instance = (TestClassWithSingleTestForTiming) testClass.instance;
      ChronoLocalDateTime setupCallTime = (ChronoLocalDateTime) instance.setupCallTime;
      ChronoLocalDateTime testMethodCallTime = (ChronoLocalDateTime) instance.testMethodCallTime;

      assertThat(setupCallTime, is(lessThanOrEqualTo(testMethodCallTime)));
    }

    @Test
    public void duringRun_TestMethodGetsCalledBeforeTeardown() throws Exception {
      testMethod.run();

      TestClassWithSingleTestForTiming instance = (TestClassWithSingleTestForTiming) testClass.instance;
      ChronoLocalDateTime teardownCallTime = (ChronoLocalDateTime) instance.teardownCallTime;
      ChronoLocalDateTime testMethodCallTime = (ChronoLocalDateTime) instance.testMethodCallTime;

      assertThat(testMethodCallTime, is(lessThanOrEqualTo(teardownCallTime)));
    }
  }

  @Context
  public class GivenTestMethodMockForExceptionHandling {
    private NullPointerException failure = new NullPointerException("NULL");

    @Test
    public void whenAnyExceptionOccurs_testResultContainsFailure() throws Exception {
      testMethod = new ThrowingTestMethodMock(testClass, failure);
      TestResult testResult = testMethod.run();
      assertTestResultContainsFailure(testResult, failure);
    }

    @Test
    public void whenInvocationTargetExceptionOccurs_testResultContainsTargetFailure() throws Exception {
      testMethod = new ThrowingTestMethodMock(testClass, new InvocationTargetException(failure));
      TestResult testResult = testMethod.run();
      assertTestResultContainsFailure(testResult, failure);
    }

    private void assertTestResultContainsFailure(TestResult testResult, Throwable failure) {
      assertThat(testResult.isSuccessful(), is(false));
      assertThat(((TestResultEntry)testResult).getFailure(), is(failure));
    }
  }
}