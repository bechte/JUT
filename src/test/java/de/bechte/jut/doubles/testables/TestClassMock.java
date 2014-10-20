/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.testables;

import de.bechte.jut.core.TestResult;
import de.bechte.jut.testables.TestClass;

public class TestClassMock<T> extends TestClass<T> {
  public static final String TEST_CLASS_NAME = "NAME";
  public static final String CANONICAL_NAME = "CANONICAL NAME";
  public static final TestResult TEST_RESULT = new PositiveTestResultStub();

  public boolean wasRun;

  public T instance;
  public Object testInstanceOnSetup;
  public Object testInstanceOnTeardown;

  public TestClassMock(T instance) {
    super((Class<T>) instance.getClass());
    this.instance = instance;
  }

  @Override
  public T createTestInstance() {
    return instance;
  }

  @Override
  public String getName() {
    return TEST_CLASS_NAME;
  }

  @Override
  public String getUniqueName() {
    return CANONICAL_NAME;
  }

  @Override
  public TestResult run() {
    wasRun = true;
    return TEST_RESULT;
  }

  @Override
  public void setupTestInstance(T testInstance) {
    testInstanceOnSetup = testInstance;
    super.setupTestInstance(testInstance);
  }

  @Override
  public void teardownTestInstance(T testInstance) {
    testInstanceOnTeardown = testInstance;
    super.teardownTestInstance(testInstance);
  }
}