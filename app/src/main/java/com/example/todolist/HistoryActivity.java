package com.example.todolist;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.todolist.data.ToDoListContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DONE_LOADER = 123;

    Uri currentDoneUri;

    HistoryCursorAdapter doneCursorAdapter;

    ListView datalistHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        datalistHistory = findViewById(R.id.listHistory);



        doneCursorAdapter = new HistoryCursorAdapter(this,
                null, false);
        datalistHistory.setAdapter(doneCursorAdapter);


        if (currentDoneUri == null) {
            setTitle("History Done Tasks");
            getSupportLoaderManager().initLoader(DONE_LOADER,
                    null, this);
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
                ToDoListContract.TaskEntry.COLUMN_TASK_DONE_STATUS,

        };

        CursorLoader cursorLoader = new CursorLoader(this,
                ToDoListContract.TaskEntry.CONTENT_URI,
                projection,
                "editStatus=?",
                new String[]{"1"},
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        doneCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        doneCursorAdapter.swapCursor(null);

    }
}
