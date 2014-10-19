package de.bechte.jut.core;

import de.bechte.jut.annotations.Test;
import de.bechte.jut.doubles.FailingTestClassSpy;
import de.bechte.jut.doubles.MultipleTestClassSpy;
import de.bechte.jut.doubles.SingleTestClassSpy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BootstrapTest {
  @Test
  public void givenSingleTest_invokesTestMethod() throws Exception {
    Bootstrap bootstrap = new Bootstrap(SingleTestClassSpy.class.getCanonicalName());
    int invocations = SingleTestClassSpy.testMethodInvocations;
    bootstrap.run();
    assertThat(SingleTestClassSpy.testMethodInvocations, is(invocations + 1));
  }

  @Test
  public void givenSingleTest_invokesBeforeMethod() throws Exception {
    Bootstrap bootstrap = new Bootstrap(SingleTestClassSpy.class.getCanonicalName());
    int invocations = SingleTestClassSpy.beforeMethodInvocations;
    bootstrap.run();
    assertThat(SingleTestClassSpy.beforeMethodInvocations, is(invocations + 1));
  }

  @Test
  public void givenSingleTest_invokesAfterMethod() throws Exception {
    Bootstrap bootstrap = new Bootstrap(SingleTestClassSpy.class.getCanonicalName());
    int invocations = SingleTestClassSpy.afterMethodInvocations;
    bootstrap.run();
    assertThat(SingleTestClassSpy.afterMethodInvocations, is(invocations + 1));
  }

  @Test
  public void givenFailingTest_invokesAfterMethod() throws Exception {
    Bootstrap bootstrap = new Bootstrap(FailingTestClassSpy.class.getCanonicalName());
    int invocations = FailingTestClassSpy.afterMethodInvocations;
    bootstrap.run();
    assertThat(FailingTestClassSpy.afterMethodInvocations, is(invocations + 1));
  }

  @Test
  public void givenMultipleTests_invokesAllMethods() throws Exception {
    Bootstrap bootstrap = new Bootstrap(MultipleTestClassSpy.class.getCanonicalName());
    int invocations1 = MultipleTestClassSpy.testMethod1Invocations;
    int invocations2 = MultipleTestClassSpy.testMethod2Invocations;
    int invocations3 = MultipleTestClassSpy.testMethod3Invocations;
    bootstrap.run();
    assertThat(MultipleTestClassSpy.testMethod1Invocations, is(invocations1 + 1));
    assertThat(MultipleTestClassSpy.testMethod2Invocations, is(invocations2 + 1));
    assertThat(MultipleTestClassSpy.testMethod3Invocations, is(invocations3 + 1));
  }

  @Test
  public void givenMultipleTests_invokesBeforeMethodForAllTests() throws Exception {
    Bootstrap bootstrap = new Bootstrap(MultipleTestClassSpy.class.getCanonicalName());
    int beforeInvocations = MultipleTestClassSpy.beforeMethodInvocations;
    int afterInvocations = MultipleTestClassSpy.afterMethodInvocations;
    bootstrap.run();
    assertThat(MultipleTestClassSpy.beforeMethodInvocations, is(beforeInvocations + 3));
    assertThat(MultipleTestClassSpy.afterMethodInvocations, is(afterInvocations + 3));
  }
}