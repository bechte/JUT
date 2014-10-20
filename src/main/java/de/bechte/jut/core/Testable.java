/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import de.bechte.jut.testables.TestClass;

public interface Testable {
  static Testable forName(String name) {
    try {
      Class testClass = Class.forName(name);
      return new TestClass(testClass);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  String getName();
  String getCanonicalName();
  TestResult runTest();
}