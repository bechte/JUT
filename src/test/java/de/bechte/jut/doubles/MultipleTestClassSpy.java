package de.bechte.jut.doubles;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Test;

public class MultipleTestClassSpy {
  public static int testMethod1Invocations;
  public static int testMethod2Invocations;
  public static int testMethod3Invocations;
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
  public void testMethod1() {
    testMethod1Invocations++;
  }

  @Test
  public void testMethod2() {
    testMethod2Invocations++;
  }

  @Test
  public void testMethod3() {
    testMethod3Invocations++;
  }
}