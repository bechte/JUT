/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.samples.MultipleTestClassSpy;
import de.bechte.jut.samples.MultipleTestClassWithIgnoreSpy;
import de.bechte.jut.samples.SingleTestClassSpy;
import de.bechte.jut.samples.TestClassWithPrivateConstructorDummy;

import static de.bechte.jut.matchers.ExpectThrowable.expectThrowable;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestClassTest {
  @Context
  public class GivenInvalidTestClass {
    @Test
    public void createInstanceFails() throws Exception {
      expectThrowable(AssertionError.class).withMessage("Class not instantiable").in(() -> {
        TestClass<TestClassWithPrivateConstructorDummy> testClass = new TestClass<>(TestClassWithPrivateConstructorDummy.class);
        testClass.createTestInstance();
      });
    }
  }

  @Context
  public class GivenSingleTest {
    TestClass<SingleTestClassSpy> testClass = new TestClass<>(SingleTestClassSpy.class);

    @Test
    public void getNameReturnsSimpleNameOfClassUnderTest() throws Exception {
      assertThat(testClass.getName(), is(SingleTestClassSpy.class.getSimpleName()));
    }

    @Test
    public void createsTestInstance() throws Exception {
      SingleTestClassSpy instance = testClass.createTestInstance();
      assertThat(instance, is(instanceOf(SingleTestClassSpy.class)));
    }

    @Test
    public void allTestsAreSelectedForRunning() throws Exception {
      assertThat(testClass.getTests().size(), is(SingleTestClassSpy.NUMBER_OF_TEST_METHODS));
    }
  }

  @Context
  public class GivenMultipleTestsInClass {
    TestClass<MultipleTestClassSpy> testClass = new TestClass<>(MultipleTestClassSpy.class);

    @Test
    public void getNameReturnsSimpleNameOfClassUnderTest() throws Exception {
      assertThat(testClass.getName(), is(MultipleTestClassSpy.class.getSimpleName()));
    }

    @Test
    public void allTestsAreSelectedForRunning() throws Exception {
      assertThat(testClass.getTests().size(), is(MultipleTestClassSpy.NUMBER_OF_TEST_METHODS));
    }
  }

  @Context
  public class GivenMultipleTestsWithIgnoreInClass {
    TestClass<MultipleTestClassWithIgnoreSpy> testClass = new TestClass<>(MultipleTestClassWithIgnoreSpy.class);

    @Test
    public void getNameReturnsSimpleNameOfClassUnderTest() throws Exception {
      assertThat(testClass.getName(), is(MultipleTestClassWithIgnoreSpy.class.getSimpleName()));
    }

    @Test
    public void onlyNonIgnoredTestsAreSelectedForRunning() throws Exception {
      assertThat(testClass.getTests().size(), is(MultipleTestClassWithIgnoreSpy.NUMBER_OF_ACTIVE_TEST_METHODS));
    }
  }
}