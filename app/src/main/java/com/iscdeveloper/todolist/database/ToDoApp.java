package com.iscdeveloper.todolist.database;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Desarrollo on 9/2/2015.
 */
public class ToDoApp extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        Iconify
                .with(new FontAwesomeModule());


        try {
            //List<TestModel> testModels = new Select().from(TestModel.class).queryList();

        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }
}
