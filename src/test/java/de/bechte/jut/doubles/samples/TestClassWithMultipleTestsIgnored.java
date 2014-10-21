/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.samples;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Ignore;
import de.bechte.jut.annotations.Test;

public class TestClassWithMultipleTestsIgnored {
  public static Long NUMBER_OF_TEST_METHODS = 3L;

  public int beforeMethodInvocations;
  public int afterMethodInvocations;

  public int testMethod1Invocations;
  public int testMethod2Invocations;
  public int testMethod3Invocations;

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