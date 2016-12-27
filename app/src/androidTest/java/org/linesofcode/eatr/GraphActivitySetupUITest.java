package org.linesofcode.eatr;

import android.support.test.rule.ActivityTestRule;

import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.eatr.framework.DisableAnimationsRule;
import org.linesofcode.eatr.day.GraphActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

public class GraphActivitySetupUITest {

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<GraphActivity> rule = new ActivityTestRule(GraphActivity.class);

    @ClassRule
    public static DisableAnimationsRule disableAnimationsRule = new DisableAnimationsRule();

    @Before
    public void setUp() {
        rule.getActivity();
    }

    @Test
    public void mainActivityShouldBeStarted() {
        Spoon.screenshot(rule.getActivity(), "mainActivityShouldBeStarted");
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void floatingActionButtonShouldBeRendered() {
        Spoon.screenshot(rule.getActivity(), "floatingActionButtonShouldBeRendered");
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerViewShouldBeRendered() {
        Spoon.screenshot(rule.getActivity(), "recyclerViewShouldBeRendered");
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void toolBarShouldBeRendered() {
        Spoon.screenshot(rule.getActivity(), "toolBarShouldBeRendered");
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeUpShouldMinimizeAppBar() {
        onView(withId(R.id.mainLayout)).perform(swipeUp());
        Spoon.screenshot(rule.getActivity(), "swipeUpShouldMinimizeAppBar");
        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
    }

    @Test
    public void swipeDownShouldMaximizeAppBar() {
        onView(withId(R.id.mainLayout)).perform(swipeUp());
        onView(withId(R.id.mainLayout)).perform(swipeDown());

        Spoon.screenshot(rule.getActivity(), "swipeDownShouldMaximizeAppBar");
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }
}