package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.data.ToDoListContract;

import java.util.Date;

public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_item, parent,
                false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView describeTheTaskTextView = view.findViewById(R.id.describeTheTaskTextView);
        TextView insertTheDateTextView = view.findViewById(R.id.insertTheDateTextView);
        CheckBox taskCheckBox = (CheckBox) view.findViewById(R.id.taskCheckBox);

        String describeTheTask = cursor.getString(cursor.getColumnIndexOrThrow
                (ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK));
        String insertTheDate = cursor.getString(cursor.getColumnIndexOrThrow
                (ToDoListContract.TaskEntry.COLUMN_TASK_DATE));
        String insertTheStatus = cursor.getString(cursor.getColumnIndexOrThrow
                (ToDoListContract.TaskEntry.COLUMN_TASK_STATUS));

        Log.d("status", "" +  insertTheStatus);
        if (insertTheStatus.equals("1")) {
            Log.d("IF", insertTheStatus);
            taskCheckBox.setChecked(true);
        } else {
            Log.d("ELSE", insertTheStatus);
            taskCheckBox.setChecked(false);
        }


        describeTheTaskTextView.setText(describeTheTask);
        insertTheDateTextView.setText(insertTheDate);
        final Context ctx = context;
        final int id = cursor.getInt(cursor.getColumnIndex(ToDoListContract.TaskEntry._ID));

        taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("isChecked", "" + isChecked);
                String selection = ToDoListContract.TaskEntry._ID + "=?";
                String itemIDArgs = Integer.toString(id);
                //This is a toast to check if the correct item is being clicked
                Log.d("ID", "" + itemIDArgs);
                //Works till here

                //Selection args claus
                String[] selectionArgs = {itemIDArgs};
                //Update the value

                ContentValues contentValues = new ContentValues();
                contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_STATUS, isChecked);

                if(isChecked) {
                    contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_DONE_STATUS, new Date().toString());
                    Toast.makeText(ctx,
                            "CHECKED",
                            Toast.LENGTH_LONG).show();
                } else {
                    contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_DONE_STATUS, "");
                    Toast.makeText(ctx,
                            "UNCHECKED",
                            Toast.LENGTH_LONG).show();
                }

                ctx.getContentResolver().update(
                        Uri.withAppendedPath(ToDoListContract.TaskEntry.CONTENT_URI,Integer.toString(id)),
                        contentValues,
                        selection,selectionArgs);
                return;
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(ToDoListContract.TaskEntry.COLUMN_TASK_STATUS, isChecked);
//                context.getContentResolver().update(currentTaskUri,
//                        contentValues, null, null);
            }
        });

    }
}
