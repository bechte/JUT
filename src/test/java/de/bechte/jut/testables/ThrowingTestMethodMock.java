/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

public class ThrowingTestMethodMock<T extends Throwable> extends TestMethod<Object> {
  private T throwable;

  public ThrowingTestMethodMock(TestClass<Object> testClass, T throwable) {
    super(testClass, null);
    this.throwable = throwable;
  }

  @Override
  protected void invokeTestMethod() throws Throwable {
    throw throwable;
  }
}