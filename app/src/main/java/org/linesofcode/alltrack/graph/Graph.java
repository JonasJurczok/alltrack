package org.linesofcode.alltrack.graph;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Copyright 2015 Jonas Jurczok (jonasjurczok@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@DatabaseTable(tableName = "graph")
public class Graph {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, uniqueIndex = true)
    private String name;

    // TODO: add icon support

    // TODO: defensive copies
    @ForeignCollectionField(orderColumnName = "datetime")
    private Collection<DataPoint> datapoints;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<DataPoint> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(Collection<DataPoint> datapoints) {
        this.datapoints = datapoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Graph graph = (Graph) o;

        if (getId() != graph.getId()) return false;
        return getName().equals(graph.getName());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
