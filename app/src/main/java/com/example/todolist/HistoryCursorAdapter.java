package com.example.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
    public void bindView(View view, Context context, final Cursor cursor) {

        TextView historyTaskTextView = view.findViewById(R.id.historyTaskTextView);
        String historyTask = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_TASK_DONE_STATUS));
        historyTaskTextView.setText(historyTask);

        TextView doneDateTextView = view.findViewById(R.id.doneDateTextView);
        String doneDate = cursor.getString(cursor.getColumnIndexOrThrow(ToDoListContract.TaskEntry.COLUMN_TASK_DATE));
        try {
            doneDateTextView.setText(changeFormat(doneDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Context ctx = context;
        final int id = cursor.getInt(cursor.getColumnIndex(ToDoListContract.TaskEntry._ID));

        CheckBox historyCheckBox = (CheckBox) view.findViewById(R.id.historyCheckBox);



//        historyTaskTextView.setText(historyTask);
//        doneDateTextView.setText(doneDate);
//        final Context ctx = context;
//        final int id = cursor.getInt(cursor.getColumnIndex(ToDoListContract.TaskEntry._ID));

        historyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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






//        view.setOnTouchListener(new OnSwipeTouchListener(ctx) {
//            public void onClick() {
//                Toast.makeText(ctx, "clicked", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onSwipeLeft() {
//                Toast.makeText(ctx, "left", Toast.LENGTH_SHORT).show();
//                showDeleteItemDialog();
//            }
//
//
//            private void showDeleteItemDialog() {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//                builder.setMessage("Do you want delete the task?");
//                builder.setPositiveButton("Delete",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                deleteItem();
//                            }
//                        });
//                builder.setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (dialog != null) {
//                                    dialog.dismiss();
//                                }
//                            }
//                        });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//            }
//
//            private void deleteItem() {
//                int rowsDeleted =
//                        ctx.getContentResolver().delete(
//                                Uri.withAppendedPath(ToDoListContract.TaskEntry.CONTENT_URI, Integer.toString(id)),
//                                null, null);
//
//                if (rowsDeleted == 0) {
//                    Toast.makeText(ctx,
//                            "Deleting of list from the table failed",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(ctx,
//                            "Task is deleted",
//                            Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });



    public String changeFormat(String date) throws ParseException {
        if (date.equals("")) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        Date convertedDate = dateFormat.parse(date);

        SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdfnewformat.format(convertedDate);

    }
}


