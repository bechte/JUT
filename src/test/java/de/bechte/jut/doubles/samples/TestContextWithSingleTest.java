/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.samples;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;

public class TestContextWithSingleTest {
  public int beforeMethodInvocations;
  public int afterMethodInvocations;
  public int testMethodInvocations;

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