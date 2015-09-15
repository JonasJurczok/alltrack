package org.linesofcode.alltrack;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityUITest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        rule.getActivity();
    }

    @Test
    public void testActivityShouldBeStarted() {
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }
}