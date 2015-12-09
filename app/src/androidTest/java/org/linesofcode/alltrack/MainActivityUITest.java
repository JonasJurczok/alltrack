package org.linesofcode.alltrack;

import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.GeneralLocation.BOTTOM_CENTER;
import static android.support.test.espresso.action.GeneralLocation.CENTER_LEFT;
import static android.support.test.espresso.action.GeneralLocation.CENTER_RIGHT;
import static android.support.test.espresso.action.GeneralLocation.TOP_CENTER;
import static android.support.test.espresso.action.Press.FINGER;
import static android.support.test.espresso.action.Swipe.FAST;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

public class MainActivityUITest {

    public static final GeneralSwipeAction SWIPE_OPEN = new GeneralSwipeAction(FAST, CENTER_LEFT, CENTER_RIGHT, FINGER);
    public static final GeneralSwipeAction SWIPE_UP = new GeneralSwipeAction(FAST, BOTTOM_CENTER, TOP_CENTER, FINGER);

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        rule.getActivity();
    }

    @Test
    public void mainActivityShouldBeStarted() {
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void floatingActionButtonShouldBeRendered() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerViewShouldBeRendered() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void toolBarShouldBeRendered() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void navigationViewShouldBeRendered() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        onView(withId(R.id.navigation)).check(matches(isDisplayed()));
    }

    @Test
    public void navigationViewShouldBeClosable() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        onView(withId(R.id.mainLayout)).perform(swipeLeft());
        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
    }

    @Test
    public void swipeUpShouldMinimizeAppBar() {
        onView(withId(R.id.mainLayout)).perform(swipeUp());
        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
    }

    @Test
    public void swipeDownShouldMaximizeAppBar() throws InterruptedException {
        onView(withId(R.id.mainLayout)).perform(swipeUp());
        onView(withId(R.id.mainLayout)).perform(swipeDown());

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }
}