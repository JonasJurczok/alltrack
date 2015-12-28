package org.linesofcode.alltrack;

import android.app.Application;

import dagger.ObjectGraph;

public class App extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(new ApplicationModule(this));
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
