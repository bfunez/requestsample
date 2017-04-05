package com.iscdeveloper.todolist.utils;

import com.iscdeveloper.todolist.database.models.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brian on 3/30/17.
 */

public class Functions {

    public static  String getDateEnglishF(String date){
        String rdate="";
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dateFormatSpanish = new SimpleDateFormat("yyyy-MM-dd");
            Date regdate = dateFormat.parse(date);
            rdate = dateFormatSpanish.format(regdate);
        }catch (Exception e){}
        return rdate;
    }

    public static  String getDateSpanishF(String date){
        String rdate="";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dateFormatSpanish = new SimpleDateFormat("dd/MM/yyyy");
            Date regdate = dateFormat.parse(date);
            rdate = dateFormatSpanish.format(regdate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rdate;
    }

    public static  String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getTaskHtmlText(Task task){
        String html="";
        html += html += "<a><big><b>" + task.task + "</b> </big><br>" +
                "<br>creado: <b>"+ getDateSpanishF(task.date) ;

//        if(task.due_date!=null) {
//            html += "<br>Fecha de Vencimeinto:" + getDateSpanishF(task.due_date);
//        }
        html += "</a>";
        return  html;
    }


}
