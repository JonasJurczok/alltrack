package org.linesofcode.alltrack;

import android.support.design.internal.NavigationMenuItemView;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.GeneralLocation.CENTER_LEFT;
import static android.support.test.espresso.action.GeneralLocation.CENTER_RIGHT;
import static android.support.test.espresso.action.Press.FINGER;
import static android.support.test.espresso.action.Swipe.FAST;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
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
public class NavigationUITest {
    public static final GeneralSwipeAction SWIPE_OPEN = new GeneralSwipeAction(FAST, CENTER_LEFT, CENTER_RIGHT, FINGER);

    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<GraphActivity> rule = new ActivityTestRule(GraphActivity.class);

    @Before
    public void setUp() {
        rule.getActivity();
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
    public void openNavFromMainShouldHaveMainChecked() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);

        onView(withText(R.string.navigation_series)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Check if menu item is checked.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                NavigationMenuItemView menuItem = (NavigationMenuItemView) view;
                assertThat(menuItem.getItemData().isChecked(), is(true));
            }
        });
    }

    @Test
    public void clickOnDataSeriesShouldCloseNavigation() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        onView(withText(R.string.navigation_series)).perform(click());

        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }
}
