package org.linesofcode.eatr;

import android.app.Application;

public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public AppComponent getComponent() {
        return component;
    }
}
