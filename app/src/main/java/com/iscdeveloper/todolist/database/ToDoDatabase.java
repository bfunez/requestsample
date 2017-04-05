package com.iscdeveloper.todolist.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Desarrollo on 9/2/2015.
 */
@Database( name = ToDoDatabase.dbname, version = ToDoDatabase.dbversion, backupEnabled = false)
public class ToDoDatabase {
    public static final String dbname= "DB_TODO";
    public static final int dbversion = 1;
}
