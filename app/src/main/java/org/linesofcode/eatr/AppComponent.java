package org.linesofcode.eatr;

import org.linesofcode.eatr.day.GraphActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {

    void inject(GraphActivity graphActivity);

    void inject(SettingsActivity settingsActivity);

}