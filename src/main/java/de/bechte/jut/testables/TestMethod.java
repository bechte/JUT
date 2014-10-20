/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestResultEntry;
import de.bechte.jut.core.Testable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestMethod<T> implements Testable {
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
  public TestResult runTest() {
    TestResultEntry testResult = new TestResultEntry(this);
    try {
      invokeTestMethod();
      testResult.success();
    } catch (InvocationTargetException e) {
      testResult.fail(e.getTargetException());
    } catch (Throwable t) {
      testResult.fail(t);
    }
    return testResult;
  }

  protected void invokeTestMethod() throws Throwable {
    T testInstance = testClass.createTestInstance();

    try {
      testClass.invokeBeforeMethods(testInstance);
      methodUnderTest.invoke(testInstance);
    } finally {
      testClass.invokeAfterMethods(testInstance);
    }
  }
}