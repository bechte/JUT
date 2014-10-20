/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.core;

import de.bechte.jut.core.Testable;
import de.bechte.jut.core.TestableFactory;
import de.bechte.jut.doubles.testables.TestableSpy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class TestableFactorySpy implements TestableFactory {
  public List<String> invocationsForName = new LinkedList<>();
  public List<Object> invocationsForObject = new LinkedList<>();
  public List<TestableSpy> allTestables = new LinkedList<>();
  public Map<Testable, List<Object>> invocationsForChild = new HashMap<>();

  @Override
  public Testable forName(String name) {
    invocationsForName.add(name);
    return getNextTestableInstance();
  }

  @Override
  public Testable forObject(Object object) {
    invocationsForObject.add(object);
    return getNextTestableInstance();
  }

  @Override
  public Testable forChild(Testable parent, Object child) {
    List<Object> objects = invocationsForChild.containsKey(parent)
        ? invocationsForChild.get(parent) : new LinkedList<>();
    objects.add(child);
    invocationsForChild.put(parent, objects);
    return getNextTestableInstance();
  }

  private TestableSpy getNextTestableInstance() {
    TestableSpy nextInstance = createInstance();
    allTestables.add(nextInstance);
    return nextInstance;
  }

  protected abstract TestableSpy createInstance();
}