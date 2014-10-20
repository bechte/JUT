/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.core;

import de.bechte.jut.doubles.testables.PositiveTestableSpy;
import de.bechte.jut.doubles.testables.TestableSpy;

public class PositiveTestableFactorySpy extends TestableFactorySpy {
  @Override
  protected TestableSpy createInstance() {
    return new PositiveTestableSpy();
  }
}