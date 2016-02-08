package org.linesofcode.alltrack;

import org.linesofcode.alltrack.graph.AddValueActivitiy;
import org.linesofcode.alltrack.graph.CreateGraphActivity;
import org.linesofcode.alltrack.graph.GraphActivity;
import org.linesofcode.alltrack.graph.GraphAdapter;
import org.linesofcode.alltrack.graph.GraphService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {

    void inject(GraphActivity graphActivity);

    void inject(AddValueActivitiy addValueActivitiy);

    void inject(CreateGraphActivity createGraphActivity);

    void inject(SettingsActivity settingsActivity);

    GraphService getGraphService();

    GraphAdapter getGraphAdapter();
}
