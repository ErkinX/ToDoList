package com.example.todolist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.todolist.data.ToDoListContract;

public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView describeTheTaskTextView = view.findViewById(R.id.describeTheTaskTextView);
        TextView insertTheDateTextView = view.findViewById(R.id.insertTheDateTextView);

        String describeTheTask = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK));
        String insertTheDate = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_TASK_DATE));


        describeTheTaskTextView.setText(describeTheTask);
        insertTheDateTextView.setText(insertTheDate);

    }
}
