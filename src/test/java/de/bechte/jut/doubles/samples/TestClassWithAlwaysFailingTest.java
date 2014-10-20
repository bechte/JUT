/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.samples;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Test;

public class TestClassWithAlwaysFailingTest {
  public final AssertionError assertionError = new AssertionError("Always fail!");

  public int beforeMethodInvocations;
  public int afterMethodInvocations;
  public int testMethodInvocations;

  @Before
  public void setUp() {
    beforeMethodInvocations++;
  }

  @After
  public void tearDown() {
    afterMethodInvocations++;
  }

  @Test
  public void testMethod() {
    testMethodInvocations++;
    throw assertionError;
  }
}