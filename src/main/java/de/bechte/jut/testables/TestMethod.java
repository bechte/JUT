/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.Ignore;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestStatus;
import de.bechte.jut.core.Testable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class TestMethod<T> implements Testable {
  protected static final String UNIQUE_NAME_FORMAT = "%s.%s";

  private TestClass<T> testClass;
  private Method methodUnderTest;

  public TestMethod(TestClass<T> testClass, Method methodUnderTest) {
    this.testClass = testClass;
    this.methodUnderTest = methodUnderTest;
  }

  @Override
  public String getName() {
    return methodUnderTest.getName();
  }

  @Override
  public String getUniqueName() {
    return String.format(UNIQUE_NAME_FORMAT, testClass.getUniqueName(), getName());
  }

  @Override
  public TestResult run() {
    if (methodUnderTest.isAnnotationPresent(Ignore.class))
      return new TestResult(this, TestStatus.SKIPPED, Duration.ZERO);

    LocalDateTime start = LocalDateTime.now();
    try {
      invokeTestMethod();
      return handleResponse(start);
    } catch (InvocationTargetException e) {
      return handleException(start, e.getTargetException());
    } catch (Throwable t) {
      return handleException(start, t);
    }
  }

  protected void invokeTestMethod() throws Throwable {
    T testInstance = testClass.createTestInstance();

    try {
      testClass.setupTestInstance(testInstance);
      methodUnderTest.invoke(testInstance);
    } finally {
      testClass.teardownTestInstance(testInstance);
    }
  }

  protected TestResult handleResponse(LocalDateTime start) {
    Duration duration = Duration.between(start, LocalDateTime.now());
    return new TestResult(this, TestStatus.SUCCEEDED, duration);
  }

  protected TestResult handleException(LocalDateTime start, Throwable throwable) {
    Duration duration = Duration.between(start, LocalDateTime.now());
    TestResult testResult = new TestResult(this, TestStatus.FAILED, duration);
    testResult.addFailure(throwable);
    return testResult;
  }
}