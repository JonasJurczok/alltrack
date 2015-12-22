package org.linesofcode.alltrack.graph;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Graph extends RealmObject {

    @PrimaryKey
    private String name;

    // TODO: add icon support

    // TODO: defensive copies
    private RealmList<DataPoint> datapoints = new RealmList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<DataPoint> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(RealmList<DataPoint> datapoints) {
        this.datapoints = datapoints;
    }
}
