package org.linesofcode.eatr;

import android.support.design.internal.NavigationMenuItemView;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.squareup.spoon.Spoon;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.linesofcode.eatr.day.GraphActivity;

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
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

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
        Spoon.screenshot(rule.getActivity(), "navigationViewShouldBeRendered");
        onView(withId(R.id.navigation)).check(matches(isDisplayed()));
    }

    @Test
    public void navigationViewShouldBeClosable() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        onView(withId(R.id.mainLayout)).perform(swipeLeft());
        Spoon.screenshot(rule.getActivity(), "navigationViewShouldBeClosable");
        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
    }

    @Test
    public void openNavFromMainShouldHaveMainChecked() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        Spoon.screenshot(rule.getActivity(), "openNavFromMainShouldHaveMainChecked");

        onView(withText(R.string.navigation_graphs)).perform(new ViewAction() {
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
    public void openNavFromSettingsShouldHaveSettingsChecked() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);

        onView(withText(R.string.navigation_settings)).perform(click());

        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        Spoon.screenshot(rule.getActivity(), "openNavFromSettingsShouldHaveSettingsChecked");
        onView(allOf(withText(R.string.navigation_settings), withClassName(endsWith("MenuItemView")))).perform(new ViewAction() {
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
        onView(withText(R.string.navigation_graphs)).perform(click());

        Spoon.screenshot(rule.getActivity(), "clickOnDataSeriesShouldCloseNavigation");
        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnSettingsShouldSwitchActivity() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        onView(withText(R.string.navigation_settings)).perform(click());

        Spoon.screenshot(rule.getActivity(), "clickOnSettingsShouldSwitchActivity");
        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
        onView(withText(R.string.settings_temp_label)).check(matches(isDisplayed()));
    }

    @Test
    public void navigatingBackToGraphActivityShouldHaveNavigationClosed() {
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        onView(withText(R.string.navigation_settings)).perform(click());

        Spoon.screenshot(rule.getActivity(), "navigatingBackToGraphActivityShouldHaveNavigationClosed_first_click");
        onView(withId(R.id.mainLayout)).perform(SWIPE_OPEN);
        onView(withText(R.string.navigation_graphs)).perform(click());

        Spoon.screenshot(rule.getActivity(), "navigatingBackToGraphActivityShouldHaveNavigationClosed_final_click");
        onView(withId(R.id.navigation)).check(matches(not(isDisplayed())));
    }
}
