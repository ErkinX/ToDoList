package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private List<RecyclerHistoryEntity> data;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView taskTextView;
        private TextView doneDateTextView;

        public MyViewHolder(View itemView) {
            super(itemView);

            taskTextView = itemView.findViewById(R.id.historyTaskTextView);
            doneDateTextView = itemView.findViewById(R.id.doneDateTextView);
        }
    }

    public RecyclerViewAdapter(List<RecyclerHistoryEntity> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecyclerHistoryEntity entity = data.get(position);

        if(holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).taskTextView.setText(entity.getTask());

            try {
                ((MyViewHolder) holder).doneDateTextView.setText(changeFormat(entity.getDoneDate()));
            } catch (ParseException e) {
                e.printStackTrace();
                ((MyViewHolder) holder).doneDateTextView.setText(entity.getDoneDate());
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String changeFormat(String date) throws ParseException {
        if (date.equals("")) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date convertedDate = dateFormat.parse(date);

        SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd/MM/yyyy");
        return sdfnewformat.format(convertedDate);

    }

    public void removeItem(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    public RecyclerHistoryEntity getEntity(int adapterPosition) {
        return data.get(adapterPosition);
    }

    public void undoDelete(RecyclerHistoryEntity entity, int position) {
        data.add(position, entity);
        notifyDataSetChanged();
    }

}

