/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.doubles.MethodInvocationCounter;

import java.lang.reflect.InvocationTargetException;

public class TestClassSpy<T> extends TestClass<T> {
  public static final String TEST_CLASS_NAME = "NAME";

  public T instance;
  public MethodInvocationCounter invocationCounter;
  public Object testInstanceForInvokeBeforeMethods;
  public Object testInstanceForInvokeAfterMethods;

  public TestClassSpy(T instance) {
    super((Class<T>) instance.getClass());
    this.instance = instance;
    invocationCounter = new MethodInvocationCounter();
  }

  @Override
  public T createTestInstance() {
    invocationCounter.methodInvoked("createTestInstance");
    return instance;
  }

  @Override
  public String getName() {
    invocationCounter.methodInvoked("getName");
    return TEST_CLASS_NAME;
  }

  @Override
  public TestResult runTest() {
    invocationCounter.methodInvoked("runTest");
    return null;
  }

  @Override
  public void invokeBeforeMethods(Object testInstance) throws InvocationTargetException, IllegalAccessException {
    invocationCounter.methodInvoked("invokeBeforeMethods");
    testInstanceForInvokeBeforeMethods = testInstance;
  }

  @Override
  public void invokeAfterMethods(Object testInstance) throws InvocationTargetException, IllegalAccessException {
    invocationCounter.methodInvoked("invokeAfterMethods");
    testInstanceForInvokeAfterMethods = testInstance;
  }
}