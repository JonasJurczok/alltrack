package org.linesofcode.eatr.framework.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * This TextView ensures that all instances of it will keep scrolling horizontally.
 * Taken from http://stackoverflow.com/questions/8455915/android-multiple-textview-marquee#answer-21112501
 */
public class ScrollingTextview extends TextView {
    public ScrollingTextview(Context context) {
        super(context);
    }

    public ScrollingTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollingTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused)
            super.onWindowFocusChanged(focused);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
