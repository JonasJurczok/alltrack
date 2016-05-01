package org.linesofcode.alltrack.framework.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.linesofcode.alltrack.graph.DataPoint;
import org.linesofcode.alltrack.graph.Graph;

import java.sql.SQLException;

import static com.j256.ormlite.table.TableUtils.createTable;

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
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String LOGTAG = DatabaseHelper.class.getName();

    private static final String DB_NAME = "alltrack_database.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(LOGTAG, "Creating Datapoint table.");
            createTable(connectionSource, DataPoint.class);
            Log.i(LOGTAG, "Creating Graph table.");
            createTable(connectionSource, Graph.class);
            Log.i(LOGTAG, "Table creation finished.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            database.execSQL("ALTER TABLE graph ADD COLUMN type varchar not null default 'NUMBERS;'");
        }
    }
}
