package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.view.GestureDetectorCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.data.ToDoListContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER = 123;

    TaskCursorAdapter taskCursorAdapter;

    ListView dataListView;

    private EditText describeTheTaskEditText;
    private EditText taskDateEditText;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_item:
                history();
        }
        return super.onOptionsItemSelected(item);
    }

    private void history() {
        Intent intent = new Intent(MainActivity.this,
                HistoryActivity.class);
        startActivity(intent);
    }

    private void showDeleteHistoryDialog() {
    }

    private void historyDoneTasks() {

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

//        if (currentTaskUri == null) {

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
//        } else {
//            int rowsChanged = getContentResolver().update(currentTaskUri,
//                    contentValues, null, null);

//            if (rowsChanged == 0) {
        Toast.makeText(this,
                "Saving of task in the table failed",
                Toast.LENGTH_LONG).show();
//            } else {
        Toast.makeText(this,
                "Task updated", Toast.LENGTH_LONG).show();
    }
//        }


//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListView = findViewById(R.id.listTasks);

        FloatingActionButton floatingActionButton =
                findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        AddTaskActivity.class);
                startActivity(intent);

            }
        });

        taskCursorAdapter = new TaskCursorAdapter(this,
                null, false);
        dataListView.setAdapter(taskCursorAdapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(MainActivity.this,
                        AddTaskActivity.class);
                Uri currentTaskUri = ContentUris
                        .withAppendedId(ToDoListContract.TaskEntry.CONTENT_URI, id);
                intent.setData(currentTaskUri);
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(TASK_LOADER,
                null, this);


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

        CursorLoader cursorLoader = new CursorLoader(this,
                ToDoListContract.TaskEntry.CONTENT_URI,
                projection,
                "editStatus=?",
                new String[]{"false"},
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        taskCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        taskCursorAdapter.swapCursor(null);

    }
}