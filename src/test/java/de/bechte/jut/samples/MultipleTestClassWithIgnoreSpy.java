/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.samples;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Ignore;
import de.bechte.jut.annotations.Test;

public class MultipleTestClassWithIgnoreSpy {
  public static int NUMBER_OF_ACTIVE_TEST_METHODS = 1;

  public static int beforeMethodInvocations;
  public static int afterMethodInvocations;
  public static int testMethod1Invocations;
  public static int testMethod2Invocations;
  public static int testMethod3Invocations;

  @Before
  @Ignore
  public void setUp() {
    beforeMethodInvocations++;
  }

  @After
  @Ignore
  public void tearDown() {
    afterMethodInvocations++;
  }

  @Test
  public void testMethod1() {
    testMethod1Invocations++;
  }

  @Test
  @Ignore
  public void testMethod2() {
    testMethod2Invocations++;
  }

  @Test
  @Ignore
  public void testMethod3() {
    testMethod3Invocations++;
  }
}