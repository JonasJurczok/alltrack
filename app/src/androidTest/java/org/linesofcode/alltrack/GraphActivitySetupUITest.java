package org.linesofcode.alltrack;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.alltrack.framework.DisableAnimationsRule;
import org.linesofcode.alltrack.graph.GraphActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

/**
 * Copyright 2015 Jonas Jurczok (jonasjurczok@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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