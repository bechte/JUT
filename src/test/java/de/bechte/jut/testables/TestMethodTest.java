/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestResultEntry;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestMethodTest {
  public static final String TEST_METHOD_NAME = "toString";

  private TestClassSpy<Object> testClass = new TestClassSpy(new Object());
  private TestMethod<Object> testMethod;

  @Context
  public class GivenValidTestMethod {
    @Before
    public void createTestMethod() throws Exception {
      testMethod = new TestMethod<>(testClass, Object.class.getMethod(TEST_METHOD_NAME));
    }

    @Test
    public void getNameReturnsNameOfMethod() throws Exception {
      assertThat(testMethod.getName(), is(TEST_METHOD_NAME));
    }

    @Test
    public void getCanonicalNameReturnsFullyQualifiedNameOfMethod() throws Exception {
      assertThat(testMethod.getCanonicalName(), is(String.format(
          TestMethod.CANONICAL_PATTERN, testClass.getCanonicalName(), TEST_METHOD_NAME)));
    }

    @Test
    public void runTestMethodInvokesMethodsOfTheTestClass() throws Throwable {
      testMethod.invokeTestMethod();

      assertThat(testClass.invocationCounter.getInvocations("createTestInstance"), is(1));
      assertThat(testClass.invocationCounter.getInvocations("invokeBeforeMethods"), is(1));
      assertThat(testClass.invocationCounter.getInvocations("invokeAfterMethods"), is(1));
    }
  }

  @Context
  public class GivenTestMethodMockForExceptionHandling {
    @Test
    public void whenAnyExceptionOccurs_testResultContainsFailure() throws Exception {
      NullPointerException failure = new NullPointerException("NULL");
      testMethod = new ThrowingTestMethodMock(testClass, failure);

      TestResult testResult = testMethod.runTest();
      assertTestResultContainsFailure(testResult, failure);
    }

    @Test
    public void whenInvocationTargetExceptionOccurs_testResultContainsFailure() throws Exception {
      NullPointerException failure = new NullPointerException("NULL");
      InvocationTargetException exception = new InvocationTargetException(failure);
      testMethod = new ThrowingTestMethodMock(testClass, exception);

      TestResult testResult = testMethod.runTest();
      assertTestResultContainsFailure(testResult, failure);
    }

    private void assertTestResultContainsFailure(TestResult testResult, Throwable failure) {
      assertThat(testResult.isSuccessful(), is(false));
      assertThat(((TestResultEntry)testResult).getFailure(), is(failure));
    }
  }
}