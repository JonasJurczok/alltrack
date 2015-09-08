package org.linesofcode.alltrack;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView view = (TextView) findViewById(R.id.text_view);
        view.setText("Hello World!");
    }
}
