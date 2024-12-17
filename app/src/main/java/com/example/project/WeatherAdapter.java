package com.example.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<Weather.Item> items = new ArrayList<>();

    public void setItems(List<Weather.Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(Weather.Item item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    @NonNull
    @Override
    public WeatherAdapter.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        Weather.Item item = items.get(position);
        holder.time_tci.setText(item.tm);
        holder.city_name.setText(item.totalCityName);
        holder.tci.setText(item.kmaTci);

        switch (item.TCI_GRADE) {
            case "매우좋음":
                holder.tci_grade.setImageResource(R.drawable.very_good);
                holder.itemView.setBackgroundResource(R.drawable.sky_blue);
                break;
            case "좋음":
                holder.tci_grade.setImageResource(R.drawable.good);
                holder.itemView.setBackgroundResource(R.drawable.green);
                break;
            case "보통":
                holder.tci_grade.setImageResource(R.drawable.normal);
                holder.itemView.setBackgroundResource(R.drawable.yellow);
                break;
            default:
                holder.tci_grade.setImageResource(R.drawable.bad);
                holder.itemView.setBackgroundResource(R.drawable.red);
                break;
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView time_tci;
        TextView city_name;
        TextView tci;
        ImageView tci_grade;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            time_tci = itemView.findViewById(R.id.time_tci);
            city_name = itemView.findViewById(R.id.city_name);
            tci = itemView.findViewById(R.id.tci);
            tci_grade = itemView.findViewById(R.id.tciImage);
        }
    }

    public void clearItem() {
        items.clear();
    }
}
