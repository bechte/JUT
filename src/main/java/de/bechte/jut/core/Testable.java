package de.bechte.jut.core;

import de.bechte.jut.testables.TestClass;

import java.util.Collection;

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
  Collection<? extends TestResult> runTests();
}