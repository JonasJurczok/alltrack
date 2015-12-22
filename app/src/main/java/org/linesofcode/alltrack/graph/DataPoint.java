package org.linesofcode.alltrack.graph;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class DataPoint extends RealmObject {

    @Required
    private Date datetime;

    private int value;

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
