/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.samples.FailingTestClassSpy;
import de.bechte.jut.samples.MultipleTestClassSpy;
import de.bechte.jut.samples.MultipleTestClassWithIgnoreSpy;
import de.bechte.jut.samples.SingleTestClassSpy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BootstrapTest {
  private Bootstrap bootstrap;

  @Context
  public class GivenFailingTestInClass {
    @Before
    public void initFailingTest() {
      bootstrap = new Bootstrap(FailingTestClassSpy.class.getCanonicalName());
    }

    @Test
    public void invokesAfterMethod() throws Exception {
      int invocations = FailingTestClassSpy.afterMethodInvocations;
      bootstrap.run();
      assertThat(FailingTestClassSpy.afterMethodInvocations, is(invocations + 1));
    }
  }

  @Context
  public class GivenSingleTestInClass {
    @Before
    public void initSingleTest() {
      bootstrap = new Bootstrap(SingleTestClassSpy.class.getCanonicalName());
    }

    @Test
    public void givenSingleTest_invokesTestMethod() throws Exception {
      int invocations = SingleTestClassSpy.testMethodInvocations;
      bootstrap.run();
      assertThat(SingleTestClassSpy.testMethodInvocations, is(invocations + 1));
    }

    @Test
    public void givenSingleTest_invokesBeforeMethod() throws Exception {
      int invocations = SingleTestClassSpy.beforeMethodInvocations;
      bootstrap.run();
      assertThat(SingleTestClassSpy.beforeMethodInvocations, is(invocations + 1));
    }

    @Test
    public void givenSingleTest_invokesAfterMethod() throws Exception {
      int invocations = SingleTestClassSpy.afterMethodInvocations;
      bootstrap.run();
      assertThat(SingleTestClassSpy.afterMethodInvocations, is(invocations + 1));
    }
  }

  @Context
  public class GivenMultipleTestsInClass {
    @Before
    public void initMultipleTests() {
      bootstrap = new Bootstrap(MultipleTestClassSpy.class.getCanonicalName());
    }

    @Test
    public void invokesAllTests() throws Exception {
      int invocations1 = MultipleTestClassSpy.testMethod1Invocations;
      int invocations2 = MultipleTestClassSpy.testMethod2Invocations;
      int invocations3 = MultipleTestClassSpy.testMethod3Invocations;

      bootstrap.run();

      assertThat(MultipleTestClassSpy.testMethod1Invocations, is(invocations1 + 1));
      assertThat(MultipleTestClassSpy.testMethod2Invocations, is(invocations2 + 1));
      assertThat(MultipleTestClassSpy.testMethod3Invocations, is(invocations3 + 1));
    }

    @Test
    public void invokesBeforeAndAfter_forAllTests() throws Exception {
      int beforeInvocations = MultipleTestClassSpy.beforeMethodInvocations;
      int afterInvocations = MultipleTestClassSpy.afterMethodInvocations;

      bootstrap.run();

      assertThat(MultipleTestClassSpy.beforeMethodInvocations, is(beforeInvocations + 3));
      assertThat(MultipleTestClassSpy.afterMethodInvocations, is(afterInvocations + 3));
    }
  }

  @Context
  public class GivenMultipleTestsWithIgnoreInClass {
    @Before
    public void initMultipleTests() {
      bootstrap = new Bootstrap(MultipleTestClassWithIgnoreSpy.class.getCanonicalName());
    }

    @Test
    public void invokesOnlyNonIgnoredTests() throws Exception {
      int invocations1 = MultipleTestClassWithIgnoreSpy.testMethod1Invocations;
      int invocations2 = MultipleTestClassWithIgnoreSpy.testMethod2Invocations;
      int invocations3 = MultipleTestClassWithIgnoreSpy.testMethod3Invocations;

      bootstrap.run();

      assertThat(MultipleTestClassWithIgnoreSpy.testMethod1Invocations, is(invocations1 + 1));
      assertThat(MultipleTestClassWithIgnoreSpy.testMethod2Invocations, is(invocations2));
      assertThat(MultipleTestClassWithIgnoreSpy.testMethod3Invocations, is(invocations3));
    }

    @Test
    public void doesNotInvokeIgnoredBeforeAndAfter_forAnyTests() throws Exception {
      int beforeInvocations = MultipleTestClassWithIgnoreSpy.beforeMethodInvocations;
      int afterInvocations = MultipleTestClassWithIgnoreSpy.afterMethodInvocations;

      bootstrap.run();

      assertThat(MultipleTestClassWithIgnoreSpy.beforeMethodInvocations, is(beforeInvocations));
      assertThat(MultipleTestClassWithIgnoreSpy.afterMethodInvocations, is(afterInvocations));
    }
  }
}