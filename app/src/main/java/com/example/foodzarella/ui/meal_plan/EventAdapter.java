package com.example.foodzarella.ui.meal_plan;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;
import com.example.foodzarella.ui.meal_plan.Event;
import com.example.foodzarella.ui.meal_plan.CalendarUtils;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> events;
    private Context context;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        String eventTitle = event.getName() + " " + CalendarUtils.formattedTime(event.getTime());
        holder.eventCellTV.setText(eventTitle);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventCellTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventCellTV = itemView.findViewById(R.id.eventCellTV);
        }
    }
}
