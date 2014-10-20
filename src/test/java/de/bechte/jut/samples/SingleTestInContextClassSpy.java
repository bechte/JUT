/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.samples;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;

public class SingleTestInContextClassSpy {
  public static int NUMBER_OF_TEST_METHODS = 1;

  public static int beforeMethodInvocations;
  public static int afterMethodInvocations;
  public static int testMethodInvocations;

  @Context
  public class TestContext {
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
    }
  }
}