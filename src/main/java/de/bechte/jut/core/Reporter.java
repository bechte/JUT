/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import java.util.Collection;

public interface Reporter {
  void report(Collection<TestResult> testResults);
}