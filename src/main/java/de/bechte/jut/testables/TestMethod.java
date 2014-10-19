package de.bechte.jut.testables;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.Testable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

public class TestMethod<T> implements Testable {
  private TestClass<T> testClass;
  private Method methodUnterTest;

  public TestMethod(TestClass<T> testClass, Method methodUnterTest) {
    this.testClass = testClass;
    this.methodUnterTest = methodUnterTest;
  }

  @Override
  public String getName() {
    return String.format("%s#%s", testClass.getName(), methodUnterTest.getName());
  }

  @Override
  public Collection<TestResult> runTests() {
    TestResult testResult = new TestResult(this);
    try {
      invokeTestMethod();
      testResult.success();
    } catch (InvocationTargetException e) {
      testResult.fail(e.getTargetException());
    } catch (Throwable t) {
      testResult.fail(t);
    }
    return Collections.singleton(testResult);
  }

  private void invokeTestMethod() throws IllegalAccessException, InvocationTargetException {
    T testInstance = testClass.createTestInstance();

    try {
      testClass.invokeMethodsForAnnotation(testInstance, Before.class);
      methodUnterTest.invoke(testInstance);
    } finally {
      testClass.invokeMethodsForAnnotation(testInstance, After.class);
    }
  }
}