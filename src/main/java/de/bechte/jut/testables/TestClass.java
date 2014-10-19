package de.bechte.jut.testables;

import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.Testable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Stream;

import static de.bechte.jut.core.util.ReflectionUtils.createDeepInstance;
import static de.bechte.jut.core.util.ReflectionUtils.getClassHierarchy;
import static java.util.stream.Collectors.*;

public class TestClass<T> implements Testable {
  private Class<T> classUnderTest;

  public TestClass(Class<T> classUnderTest) {
    this.classUnderTest = classUnderTest;
  }

  public T createTestInstance() {
    try {
      final Stack<Class<?>> classHierarchy = getClassHierarchy(classUnderTest);
      return (T) createDeepInstance(classHierarchy);
    } catch (Throwable t) {
      throw new AssertionError("Class not instantiable: " + classUnderTest.getTypeName(), t);
    }
  }

  @Override
  public String getName() {
    return classUnderTest.getSimpleName();
  }

  @Override
  public Collection<? extends TestResult> runTests() {
    Collection<TestResult> testResults = new LinkedList<>();

    for (Testable test : getTests())
      testResults.addAll(test.runTests());

    return testResults;
  }

  private Collection<? extends Testable> getTests() {
    Collection<Testable> tests = new LinkedList<>();
    tests.addAll(getTestMethods());
    tests.addAll(getTestClasses());
    return tests;
  }

  private Collection<? extends Testable> getTestClasses() {
    return Stream.of(classUnderTest.getClasses())
        .filter(c -> c.getAnnotationsByType(Context.class).length == 1)
        .map(c -> new TestClass(c))
        .collect(toCollection(LinkedList::new));
  }

  private Collection<? extends Testable> getTestMethods() {
    return Stream.of(classUnderTest.getMethods())
        .filter(m -> m.getAnnotationsByType(Test.class).length == 1)
        .map(m -> new TestMethod(TestClass.this, m))
        .collect(toCollection(LinkedList::new));
  }

  public void invokeMethodsForAnnotation(T testInstance, Class<? extends Annotation> annotation) throws InvocationTargetException, IllegalAccessException {
    Collection<Method> annotatedMethods = getMethodsForAnnotation(annotation);
    for (Method m : annotatedMethods)
      m.invoke(testInstance);
  }

  private Collection<Method> getMethodsForAnnotation(Class<? extends Annotation> annotation) {
    return Stream.of(classUnderTest.getMethods())
        .filter(c -> c.getAnnotationsByType(annotation).length == 1)
        .collect(toCollection(LinkedList::new));
  }
}