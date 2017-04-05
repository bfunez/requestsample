package com.iscdeveloper.todolist.database.models;
import com.iscdeveloper.todolist.database.ToDoDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = ToDoDatabase.class)
public class Task extends BaseModel implements Serializable{

    @PrimaryKey
    public long id;

    @Column
    public String task;

    @Column
    public String date;

    @Column
    public String due_date;

    @Column
    public boolean done;

}
