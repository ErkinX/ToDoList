package com.example.todolist;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.data.ToDoListContentProvider;
import com.example.todolist.data.ToDoListContract;
import com.example.todolist.data.ToDoListDbOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private static final int DONE_LOADER = 123;

    RecyclerViewAdapter historyViewAdapter;
    RecyclerView historyListView;
    RelativeLayout history;

    private Uri currentListUri;
    ArrayList<RecyclerHistoryEntity> historyList;
    private ToDoListDbOpenHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        history = findViewById(R.id.history);
        historyListView = findViewById(R.id.listHistory);

        mDatabase = new ToDoListDbOpenHelper(this);
        ArrayList<RecyclerHistoryEntity> historyList = mDatabase.listHistory();

        historyViewAdapter = new RecyclerViewAdapter(historyList);
        historyListView.setLayoutManager(new LinearLayoutManager(this));
        historyListView.setAdapter(historyViewAdapter);

        Intent intent = getIntent();

        currentListUri = intent.getData();

        if (currentListUri == null) {
            setTitle("History");
            invalidateOptionsMenu();
        }

        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_delete_24);
            private final ColorDrawable background = new ColorDrawable(Color.LTGRAY);

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final RecyclerHistoryEntity entity = historyViewAdapter.getEntity(viewHolder.getAdapterPosition());
                getContentResolver().delete(
                        Uri.withAppendedPath(ToDoListContract.TaskEntry.CONTENT_URI, Integer.toString(entity.getId())),
                        null, null);
                historyViewAdapter.removeItem(viewHolder.getAdapterPosition());

                Snackbar snackbar = Snackbar.make(history, "Item deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                historyViewAdapter.undoDelete(entity, position);
                            }
                        });
                snackbar.show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                if (dX > 0) {
                    int iconLeft = itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getLeft() + iconMargin;

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                deleteIcon.draw(c);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(historyListView);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





}
