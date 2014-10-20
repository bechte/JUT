/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

public interface Testable {
  String getName();
  String getUniqueName();
  TestResult run();
}