/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.matchers;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Ignore;
import de.bechte.jut.annotations.Test;

import static de.bechte.jut.matchers.ExpectThrowable.expectThrowable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExpectThrowableTest {
  ExpectThrowable expectThrowable = expectThrowable(RuntimeException.class);

  @Context
  public class WithoutMessageMatching {
    @Test
    public void whenTheCorrectExceptionIsThrown_itShouldReportNoAssertionError() throws Exception {
      try {
        expectThrowable.in(() -> { throw new RuntimeException(); });
      } catch (Throwable t) {
        assertThat(String.format(
            "\nExpected: There should be no exception.\n" +
              "     but: is a <%s>",
            t.getClass().getCanonicalName()), false);
      }
    }

    @Test
    public void whenTheWrongExceptionIsThrown_itShouldThrowAnAssertionError() throws Exception {
      try {
        expectThrowable.in(() -> { throw new InternalError(); });
      } catch (AssertionError e) {
        assertThat(e.getMessage(), is(String.format(
            "\nExpected: is an instance of %1$s\n" +
                "     but: <%2$s> is a %2$s",
            RuntimeException.class.getCanonicalName(), InternalError.class.getCanonicalName())));
        return;
      }

      assertThat(String.format(
          "\nExpected: is an instance of %s\n" +
              "     but: no exception was thrown!",
          AssertionError.class.getCanonicalName()), false);
    }

    @Test
    public void whenNoExceptionIsThrown_itShouldThrowAnAssertionError() throws Exception {
      try {
        expectThrowable.in(() -> { });
        assertThat(String.format(
            "Expected: <%s>.\n     but: no exception was thrown!",
            AssertionError.class.getCanonicalName()), false);
      } catch (AssertionError e) {
        assertThat(e.getMessage(), is("Expected test to throw is an instance of java.lang.RuntimeException"));
      }
    }
  }

  @Context
  public class WithMessageStringMatching {
    @Before
    public void setErrorMessage() throws Exception {
      expectThrowable.withMessage("containing");
    }

    @Test
    public void whenTheCorrectExceptionIsThrown_itShouldReportNoAssertionError() throws Exception {
      try {
        expectThrowable.in(() -> {
          throw new RuntimeException("Some text containing our keyword!");
        });
      } catch (Throwable t) {
        assertThat(String.format(
            "\nExpected: There should be no exception.\n" +
                "     but: is a <%s>",
            t.getClass().getCanonicalName()), false);
      }
    }

    @Test
    public void whenAnExceptionWithAWrongMessageIsThrown_itShouldThrowAnAssertionError() throws Exception {
      String message = "Some text without our keyword!";
      try {
        expectThrowable.in(() -> {
          throw new RuntimeException(message);
        });
        assertThat(String.format(
            "Expected: <%s>.\n     but: no exception was thrown!",
            AssertionError.class.getCanonicalName()), false);
      } catch (Throwable t) {
        assertThat(t.getMessage(), is(String.format(
            "\nExpected: (is an instance of %1$s and exception with message a string containing \"containing\")\n" +
              "     but: exception with message a string containing \"containing\" was <%1$s: %2$s>",
            RuntimeException.class.getCanonicalName(), message)));
      }
    }
  }

  @Context
  public class WithMessageMatching {
    @Test
    public void whenAnExceptionWithAPartialWrongMessageIsThrown_itShouldThrowAnAssertionError() throws Exception {
      String message = "Text with missing fullstop";
      try {
        expectThrowable.withMessage(is(message + "."));
        expectThrowable.in(() -> {
          throw new RuntimeException(message);
        });
      } catch (Throwable t) {
        assertThat(t.getMessage(), is(String.format(
            "\nExpected: (is an instance of %1$s and exception with message is \"%2$s.\")\n" +
                "     but: exception with message is \"%2$s.\" was <%1$s: %2$s>",
            RuntimeException.class.getCanonicalName(), message)));
        return;
      }

      assertThat(String.format(
          "Expected: <%s>.\n     but: no exception was thrown!",
          AssertionError.class.getCanonicalName()), false);
    }
  }
}