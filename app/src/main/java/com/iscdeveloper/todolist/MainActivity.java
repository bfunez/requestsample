package com.iscdeveloper.todolist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.iscdeveloper.todolist.database.models.Task;
import com.iscdeveloper.todolist.oauth.ApiResponse;
import com.iscdeveloper.todolist.oauth.OAuthUtils;
import com.iscdeveloper.todolist.utils.Functions;
import com.joanzapata.iconify.widget.IconTextView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static MainActivity QuickContext ;
    private static Task selectedTask;
    private static RecyclerView recyclerViewTaks;
    private static RunTaskAdapter taskAdapter;
    private static List<Task> listTasks = new ArrayList<>();

    private static ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        QuickContext = this;
        recyclerViewTaks = (RecyclerView) findViewById(R.id.listTasks);
        recyclerViewTaks.setLayoutManager(new LinearLayoutManager(getApplication()));
        new getDataRunMuWeb().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public  static class RunTaskAdapter extends RecyclerView.Adapter<RunTaskAdapter.RunTaskViewHolder> {
        private final List<Task> listTask;
        private final LayoutInflater inflater;

        public void clear(){
            if(listTask!=null) {
                listTask.clear();
            }
        }

        public RunTaskAdapter(List<Task> listTasks) {
            this.inflater = LayoutInflater.from(QuickContext);
            this.listTask = listTasks;
        }

        @Override
        public RunTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.singleobjectwithdelte, parent, false);
            RunTaskViewHolder holder = new RunTaskViewHolder(view);
            return holder;
        }

        public void animateTo(List<Task> models) {
            applyAndAnimateRemovals(models);
            applyAndAnimateAdditions(models);
            applyAndAnimateMovedItems(models);
        }

        private void applyAndAnimateRemovals(List<Task> newModels) {
            for (int i = listTask.size() - 1; i >= 0; i--) {
                final Task model = listTask.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<Task> newModels) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                final Task model = newModels.get(i);
                if (!listTask.contains(model)) {
                    addItem(i, model);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<Task> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final Task model = newModels.get(toPosition);
                final int fromPosition = listTask.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }

        public Task removeItem(int position) {
            final Task model = listTask.remove(position);
            notifyItemRemoved(position);
            return model;
        }

        public void addItem(int position, Task model) {
            listTask.add(position, model);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition, int toPosition) {
            final Task model = listTask.remove(fromPosition);
            listTask.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onBindViewHolder(final RunTaskViewHolder holder, final int position) {
            final Task current = listTask.get(position);
            if(selectedTask!=null) {
                if (selectedTask.id == current.id) {
                    holder.box.setBackgroundResource(R.color.accentColor);
                } else {
                    holder.box.setBackgroundResource(android.R.color.transparent);
                }
            }


            holder.box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showTask(current);
                }
            });

            holder.text.setHtmlFromString(Functions.getTaskHtmlText(current), new HtmlTextView.LocalImageGetter());
        }

        @Override
        public int getItemCount() {
            return listTask.size();
        }

        class RunTaskViewHolder extends RecyclerView.ViewHolder {
            ViewGroup box;
            HtmlTextView text;
            IconTextView icon;
            IconTextView delete;

            public RunTaskViewHolder(View itemView) {
                super(itemView);
                box = (ViewGroup) itemView.findViewById(R.id.object);
                text = (HtmlTextView) itemView.findViewById(R.id.text);
                //icon = (IconTextView) itemView.findViewById(R.id.iconT);
                //delete = (IconTextView) itemView.findViewById(R.id.deleteT);

            }
        }

        public List<Task> returnTask(){
            return listTask;
        }

    }





    /*
    * Tarea asincrona (Tarea en Segundo Plano)
    * @params string params
    * @returns List<Task>
    * En esta tarea se hace el request para descargar la informacion del servidor
    * ademas se carga la informacion insertada en la base de datos
     */
    public static class getDataRunMuWeb extends AsyncTask<String, Void, List<Task>> {

        //el metodo onPreExecute, se prepara la ativadad para empezar la tarea asincrono
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(QuickContext);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("Espere un Momento, Mientras actualizamos su informacion");
            dialog.setCancelable(false);
            dialog.show();
        }

        //el metodo onPostExecute ejecuta las acciones despues que la actividad en segundo plano
        @Override
        protected void onPostExecute(List<Task> result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(result != null){
                taskAdapter = new RunTaskAdapter(result);
                recyclerViewTaks.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();
            }
            System.gc();
        }

        //En este metodo se ejecutan todas las acciones de sengundo plano, aca se escriben todos los proceso que
        //Llevarian mucho tiempo y pueden bloquear el Main UI por mucho tiempo
        @Override
        protected List<Task> doInBackground(String... params) {
            try {

                //URL para el request
                String urlRequest = "http://todo.unadeca.net/api/task";
                JSONObject param = new JSONObject();
                //Aca hacemos el request utilizamos la clase OAuthUtils que ya mencionamos donde estan todos los metodos
                //de Request, post, get, put/patch , delete, en este caso estamos haciendo un pedido con un metodo get
                ApiResponse userObject = OAuthUtils.sendGet(urlRequest);
                if(userObject.success){
                    System.out.println(userObject.message);
                    JSONArray taskResult = new JSONArray(userObject.message);
                    JSONObject object;
                    Task task ;
                    for(int a = 0; a < taskResult.length(); a++) {
                        object = taskResult.getJSONObject(a);
                        task = new Task();
                        task.id = object.getLong("id");
                        task.task = object.getString("task");
                        task.date = object.getString("date");
                        task.due_date = object.getString("due_date");
                        task.done = (object.getInt("done")==1);
                        task.save();
                    }

                }
                listTasks = SQLite.select().from(Task.class).queryList();
                for(Task task: listTasks){
                    System.out.println("task" + task.date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listTasks;
        }
    }




}
