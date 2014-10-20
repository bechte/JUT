/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.*;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestableGroup;
import de.bechte.jut.reporting.TestResultGroup;
import de.bechte.jut.core.Testable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.bechte.jut.core.ApplicationContext.testableFactory;
import static java.util.stream.Collectors.*;

public class TestClass<T> implements TestableGroup<T> {
  protected Class<T> classUnderTest;

  public TestClass(Class<T> classUnderTest) {
    this.classUnderTest = classUnderTest;
  }

  @Override
  public String getName() {
    return classUnderTest.getSimpleName();
  }

  @Override
  public String getUniqueName() {
    return classUnderTest.getCanonicalName();
  }

  @Override
  public TestResult run() {
    TestResultGroup testResultGroup = new TestResultGroup(this);
    Collection<TestResult> testResults = getTests().stream()
        .map(t -> t.run())
        .collect(Collectors.toList());
    testResultGroup.addTestResults(testResults);
    return testResultGroup;
  }

  protected Collection<? extends Testable> getTests() {
    Collection<Testable> tests = new LinkedList<>();
    tests.addAll(getTestMethods());
    tests.addAll(getTestContexts());
    return tests;
  }

  protected Collection<? extends Testable> getTestContexts() {
    return Stream.of(classUnderTest.getClasses())
        .filter(c -> c.getAnnotationsByType(Context.class).length == 1)
        .filter(c -> c.getAnnotationsByType(Ignore.class).length == 0)
        .map(c -> testableFactory.forChild(TestClass.this, c))
        .collect(toList());
  }

  protected Collection<? extends Testable> getTestMethods() {
    return getMethodsForAnnotation(Test.class)
        .map(m -> testableFactory.forChild(TestClass.this, m))
        .collect(toList());
  }

  protected Stream<Method> getMethodsForAnnotation(Class<? extends Annotation> annotation) {
    return Stream.of(classUnderTest.getMethods())
        .filter(c -> c.getAnnotationsByType(annotation).length >= 1)
        .filter(c -> c.getAnnotationsByType(Ignore.class).length == 0);
  }

  @Override
  public T createTestInstance() {
    try {
      return classUnderTest.newInstance();
    } catch (Throwable t) {
      throw new AssertionError("Class not instantiable: " + classUnderTest.getTypeName(), t);
    }
  }

  @Override
  public void setupTestInstance(T testInstance) {
    invokeMethodsForAnnotation(testInstance, Before.class);
  }

  @Override
  public void teardownTestInstance(T testInstance) {
    invokeMethodsForAnnotation(testInstance, After.class);
  }

  protected void invokeMethodsForAnnotation(T testInstance, Class<? extends Annotation> annotation) {
    try {
      Collection<Method> annotatedMethods = getMethodsForAnnotation(annotation).collect(toList());
      for (Method m : annotatedMethods)
        m.invoke(testInstance);
    } catch (IllegalAccessException e) {
      throw new AssertionError(e.getMessage(), e);
    } catch (InvocationTargetException e) {
      throw new AssertionError(e.getTargetException().getMessage(), e.getTargetException());
    }
  }
}