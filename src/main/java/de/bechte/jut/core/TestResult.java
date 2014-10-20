/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import java.time.Duration;

public interface TestResult {
  String getName();

  String getUniqueName();

  Duration getDuration();

  boolean isSuccessful();

  long getNumberOfTests();

  long getNumberOfSuccessfulTests();

  long getNumberOfFailingTests();
}