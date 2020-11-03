package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.todolist.data.ToDoListContract;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int EDIT_TASK_LOADER = 111;
    Uri currentTaskUri;

    private EditText describeTheTaskEditText;
    private EditText taskDateEditText;


    int DIALOG_DATE = 1;
    Calendar calendar = Calendar.getInstance();
    int myyear = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();

        currentTaskUri = intent.getData();

        if (currentTaskUri == null) {
            setTitle("Add a Task");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Task");
            getSupportLoaderManager().initLoader(EDIT_TASK_LOADER,
                    null, this);
        }

        describeTheTaskEditText = findViewById(R.id.editText);
        taskDateEditText = findViewById(R.id.editDate);

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (currentTaskUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_task);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                saveTask();
                return  true;
            case R.id.delete_task:
                showDeleteTaskDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask() {

        String editText = describeTheTaskEditText.getText().toString().trim();

        if (TextUtils.isEmpty(editText)) {
            Toast.makeText(this,
                    "Input the describe the task",
                    Toast.LENGTH_LONG).show();
            return;

        }
        String editDate = taskDateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(editDate)) {
            Toast.makeText(this,
                    "Input the task date",
                    Toast.LENGTH_LONG).show();
            return;

        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK, editText);
        contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_DATE, editDate);

        if (currentTaskUri == null) {

            contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_STATUS, "false");
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(ToDoListContract.TaskEntry.CONTENT_URI,
                    contentValues);

            if (uri == null) {
                Toast.makeText(this,
                        "Insertion of task in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Task saved", Toast.LENGTH_LONG).show();

            }
        } else {
            int rowsChanged = getContentResolver().update(currentTaskUri,
                    contentValues, null, null);

            if (rowsChanged == 0) {
                Toast.makeText(this,
                        "Saving of task in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Task updated", Toast.LENGTH_LONG).show();
            }
        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                ToDoListContract.TaskEntry._ID,
                ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK,
                ToDoListContract.TaskEntry.COLUMN_TASK_DATE,
                ToDoListContract.TaskEntry.COLUMN_TASK_STATUS,

        };

        return new CursorLoader(this,
                currentTaskUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int taskColumIndex = cursor.getColumnIndex(
                    ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK
            );

            int dateColumIndex = cursor.getColumnIndex(
                    ToDoListContract.TaskEntry.COLUMN_TASK_DATE
            );

//            int statusColumIndex = cursor.getColumnIndex(
//                    ToDoListContract.TaskEntry.COLUMN_TASK_STATUS
//            );


            String describeTheTask = cursor.getString(taskColumIndex);
            String taskDate = cursor.getString(dateColumIndex);
            //String taskStatus = cursor.getString(statusColumIndex);

            describeTheTaskEditText.setText(describeTheTask);
            taskDateEditText.setText(taskDate);
           // taskDateEditText.setText(taskDate);



        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the task?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void deleteTask() {
        if (currentTaskUri != null) {
            int rowsDeleted = getContentResolver().delete(currentTaskUri,
                    null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,
                        "Deleting of task from the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Task is deleted",
                        Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }

    public void onclick(View view) {
        showDialog(DIALOG_DATE);
    }


    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myyear, month, day);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myyear = year - 2000;
            month = monthOfYear + 1;
            day = dayOfMonth;
            taskDateEditText.setText(day + "/" + month + "/" + myyear);
        }
    };



}