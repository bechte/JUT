/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

public interface TestableGroup<T> extends Testable {
  T createTestInstance();
  void setupTestInstance(T testInstance);
  void teardownTestInstance(T testInstance);
}