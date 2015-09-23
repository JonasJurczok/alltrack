package org.linesofcode.alltrack;

import android.app.Application;

import dagger.ObjectGraph;

public class App extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(new ApplicationModule());
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
