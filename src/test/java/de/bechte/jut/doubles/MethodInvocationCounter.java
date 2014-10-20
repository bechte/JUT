/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles;

import java.util.HashMap;
import java.util.Map;

public class MethodInvocationCounter {
  private Map<String, Integer> invocations = new HashMap<>();

  public void methodInvoked(String methodName) {
    invocations.put(methodName, getInvocations(methodName) + 1);
  }

  public int getInvocations(String methodName) {
    if (invocations.containsKey(methodName))
      return invocations.get(methodName);
    else
      return 0;
  }
}