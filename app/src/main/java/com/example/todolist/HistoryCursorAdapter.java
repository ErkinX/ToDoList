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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryCursorAdapter extends CursorAdapter {
    public HistoryCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.history_item, parent,
                false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView taskTextView = view.findViewById(R.id.historyTaskTextView);
        TextView doneDateTextView = view.findViewById(R.id.doneDateTextView);

        String describeTheTask = cursor.getString(cursor.getColumnIndexOrThrow
                (ToDoListContract.TaskEntry.COLUMN_DESCRIBE_THE_TASK));
//        String insertTheDate = cursor.getString(cursor.getColumnIndexOrThrow
//                (ToDoListContract.TaskEntry.COLUMN_TASK_DATE));
        String insertTheDate = cursor.getString(cursor.getColumnIndexOrThrow
                (ToDoListContract.TaskEntry.COLUMN_TASK_DONE_STATUS));



        taskTextView.setText(describeTheTask);
        try {
            doneDateTextView.setText(changeFormat(insertTheDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
public String changeFormat(String date) throws ParseException {
        if (date == ""){
            return "";

        }
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
    Date convertedDate = dateFormat.parse(date);

        SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdfnewformat.format(convertedDate);

}
}
