/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.samples;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Test;

import java.time.LocalDateTime;

public class TestClassWithSingleTestForTiming {
  public LocalDateTime setupCallTime;
  public LocalDateTime teardownCallTime;
  public LocalDateTime testMethodCallTime;

  @Before
  public void setUp() {
    setupCallTime = LocalDateTime.now();
  }

  @After
  public void tearDown() {
    teardownCallTime = LocalDateTime.now();
  }

  @Test
  public void testMethod() {
    testMethodCallTime = LocalDateTime.now();
  }
}