package org.linesofcode.alltrack;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

public class BasicGraphViewUITest {
    @Rule
    @SuppressWarnings("unchecked")
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        rule.getActivity();
    }

    /**
     * TESTMAP:
     *
     * inject graphs into the system to verify ui
     * line graph should be displayed
     * bar graph should be displayed
     *
     *
     */
}
