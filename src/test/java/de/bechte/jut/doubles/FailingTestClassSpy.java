/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Test;

public class FailingTestClassSpy {
  public static int testMethodInvocations;
  public static int beforeMethodInvocations;
  public static int afterMethodInvocations;

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
    throw new AssertionError("Always fail!");
  }
}